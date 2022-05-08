FOLDER_ID=b1g3hblb6k8v0sll50g1
NAME=todo
DB_NAME=$NAME-db
SERVICE_ACCOUNT_NAME=your-dear-admin-for-todo

yc iam service-account get --name $SERVICE_ACCOUNT_NAME --folder-id $FOLDER_ID > /dev/null || \
    yc iam service-account create --name $SERVICE_ACCOUNT_NAME --folder-id $FOLDER_ID
SERVICE_ACCOUNT_ID=$(yc iam service-account get --name $SERVICE_ACCOUNT_NAME --folder-id $FOLDER_ID | grep ^id: | awk '{print $2}')

yc resource-manager folder set-access-bindings \
    --access-binding role=admin,service-account-id=$SERVICE_ACCOUNT_ID \
    --id $FOLDER_ID \
    -y

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
