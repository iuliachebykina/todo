FOLDER_ID=b1g3hblb6k8v0sll50g1
NAME=todo
DB_NAME=$NAME-db
SERVICE_ACCOUNT_NAME=your-dear-admin
DB_USER=my-brand-new-user
DB_PSW=password-for-my-brand-new-user

yc iam service-account get --name $SERVICE_ACCOUNT_NAME --folder-id $FOLDER_ID > /dev/null || \
    yc iam service-account create --name $SERVICE_ACCOUNT_NAME --folder-id $FOLDER_ID
SERVICE_ACCOUNT_ID=$(yc iam service-account get --name $SERVICE_ACCOUNT_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')

yc resource-manager folder set-access-bindings \
    --access-binding role=admin,service-account-id=$SERVICE_ACCOUNT_ID \
    --id $FOLDER_ID \
    -y

yc vpc network create default --folder-id $FOLDER_ID > /dev/null
yc vpc subnet create default --network-name default --range 192.168.0.0/28 --folder-id $FOLDER_ID

yc managed-postgresql cluster get --name $DB_NAME --folder-id $FOLDER_ID > /dev/null || \
    yc managed-postgresql cluster create $DB_NAME --folder-id $FOLDER_ID \
    --user name=$DB_USER,password=$DB_PSW \
    --database name=$NAME,owner=my-brand-new-user \
    --network-name default \
    --resource-preset b2.nano \
    --disk-type network-hdd \
    --postgresql-version 14 \
    --host zone-id=ru-central1-a,subnet-name=default,assign-public-ip=true

CLUSTER_ID=$(yc managed-postgresql cluster get --name $DB_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')
DB_URL=$(yc managed-postgresql hosts list --limit 1 --cluster-id $CLUSTER_ID --folder-id $FOLDER_ID | sed -n 4p | awk '{print $2}')

export DB_URL=$DB_URL:6432/$NAME
echo $DB_URL
export DB_USER=$DB_USER
export DB_PSW=$DB_PSW

yc managed-postgresql cluster start $CLUSTER_ID

yc container registry get --name $NAME --folder-id $FOLDER_ID > /dev/null || \
    yc container registry create --name $NAME --folder-id $FOLDER_ID


BACKEND_NAME=todo-backend
FRONTEND_NAME=todo-frontend

CONTAINER_REGISTRY_ID=$(yc container registry get --name $NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')
BACKEND_IMAGE_URL=cr.yandex/$CONTAINER_REGISTRY_ID/$BACKEND_NAME
FRONTEND_IMAGE_URL=cr.yandex/$CONTAINER_REGISTRY_ID/$FRONTEND_NAME


docker build --build-arg DB_URL --build-arg DB_USER --build-arg DB_PSW backend -t $BACKEND_IMAGE_URL
docker push $BACKEND_IMAGE_URL

yc serverless container get --name $BACKEND_NAME --folder-id $FOLDER_ID > /dev/null || \
    yc serverless container create --name $BACKEND_NAME --folder-id $FOLDER_ID

BACKEND_CONTAINER_ID=$(yc serverless container get --name $BACKEND_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')

yc serverless container allow-unauthenticated-invoke $BACKEND_CONTAINER_ID

yc serverless container revision deploy \
    --container-id $BACKEND_CONTAINER_ID \
    --image $BACKEND_IMAGE_URL \
    --cores 1 \
    --core-fraction 25 \
    --memory 1GB \
    --execution-timeout 10s \
    --concurrency 1 \
    --service-account-id $SERVICE_ACCOUNT_ID

BACKEND_URL=$(yc serverless container get $BACKEND_NAME --folder-id $FOLDER_ID | grep url: | awk '{print $2}')todo

echo export default \'$BACKEND_URL\'\; > frontend/src/properties.tsx

docker build frontend -t $FRONTEND_IMAGE_URL
docker push $FRONTEND_IMAGE_URL

yc serverless container get --name $FRONTEND_NAME --folder-id $FOLDER_ID > /dev/null || \
    yc serverless container create --name $FRONTEND_NAME --folder-id $FOLDER_ID

FRONTEND_CONTAINER_ID=$(yc serverless container get --name $FRONTEND_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')

yc serverless container allow-unauthenticated-invoke $FRONTEND_CONTAINER_ID

yc serverless container revision deploy \
    --container-id $FRONTEND_CONTAINER_ID \
    --image $FRONTEND_IMAGE_URL \
    --cores 1 \
    --core-fraction 25 \
    --memory 1GB \
    --execution-timeout 600s \
    --concurrency 1 \
    --service-account-id $SERVICE_ACCOUNT_ID

FRONTEND_URL=$(yc serverless container get $FRONTEND_NAME --folder-id $FOLDER_ID | grep url: | awk '{print $2}')

echo Сервис готов, можете перейти по ссылке:
echo $FRONTEND_URL

BACKEND_LOCAL_URL=http://localhost:8090/todo
echo export default \'$BACKEND_LOCAL_URL\'\; > frontend/src/properties.tsx