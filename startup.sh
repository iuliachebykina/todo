echo Название каталога?
read FOLDER_NAME

FOLDER_ID=$(yc resource-manager folder get $FOLDER_NAME| grep ^id: | awk '{print $2}')
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
    --database name=todo,owner=my-brand-new-user \
    --network-name default \
    --resource-preset b2.nano \
    --disk-type network-hdd \
    --postgresql-version 14 \
    --host zone-id=ru-central1-a,subnet-name=default,assign-public-ip=true

CLUSTER_ID=$(yc managed-postgresql cluster get --name $DB_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')
DB_URL=$(yc managed-postgresql hosts list --limit 1 --cluster-id $CLUSTER_ID --folder-id $FOLDER_ID | sed -n 4p | awk '{print $2}')

echo DB_URL=$DB_URL >>  .env
echo DB_USER=$DB_USER >>  .env
echo DB_PSW=$DB_PSW >>  .env

yc managed-postgresql cluster start $CLUSTER_ID

exit 0

yc container registry get --name $NAME --folder-id $FOLDER_ID > /dev/null || \
    yc container registry create --name $NAME --folder-id $FOLDER_ID

CONTAINER_REGISTRY_ID=$(yc container registry get --name $NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')
# IMAGE_URL=cr.yandex/$CONTAINER_REGISTRY_ID/postgres

# docker pull postgres
# docker tag postgres $IMAGE_URL
# docker push $IMAGE_URL

# yc serverless container get --name $DB_NAME --folder-id $FOLDER_ID > /dev/null || \
#     yc serverless container create --name $DB_NAME --folder-id $FOLDER_ID

# CONTAINER_ID=$(yc serverless container get --name $DB_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')

# yc serverless container allow-unauthenticated-invoke $CONTAINER_ID

# yc serverless container revision deploy \
#     --container-id $CONTAINER_ID \
#     --image $IMAGE_URL \
#     --cores 1 \
#     --core-fraction 25 \
#     --memory 1GB \
#     --execution-timeout 10s \
#     --concurrency 1 \
#     --service-account-id $SERVICE_ACCOUNT_ID \
#     --environment POSTGRES_USER=postgres \
#     --environment POSTGRES_PASSWORD=postgres \
#     --environment PGDATA=/var/lib/postgresql/data/some_name/

exit 0

docker-compose up -d --build
