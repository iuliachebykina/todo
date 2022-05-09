FOLDER_ID=b1g3hblb6k8v0sll50g1
DB_NAME=todo

docker-compose -f docker-compose-postgres.yml up -d --build
docker-compose up -d --build

exit 0