FOLDER_ID=b1g3hblb6k8v0sll50g1
DB_NAME=todo

yc ydb database create $DB_NAME \
    --serverless \
    --folder-id $FOLDER_ID \
    --sls-storage-size 1GB \
    --sls-enable-throttling-rcu \
    --sls-throttling-rcu 1

DOCUMENT_API_ENDPOINT=$(yc ydb database get --name $DB_NAME --folder-id $FOLDER_ID | grep document_api_endpoint: | awk '{print $2}')
echo $DOCUMENT_API_ENDPOINT > variables/DOCUMENT_API_ENDPOINT