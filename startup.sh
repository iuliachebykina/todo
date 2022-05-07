FOLDER_ID=b1g3hblb6k8v0sll50g1
DB_NAME=todo

yc ydb database create $DB_NAME --serverless --folder-id $FOLDER_ID
DB_ID=$(yc ydb database get --name $DB_NAME --folder-id $FOLDER_ID | grep document_api_endpoint: | awk '{print $2}')
echo $DB_ID > variables/DB_ID