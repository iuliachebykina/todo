# pull official base image
FROM node:13.12.0-alpine

COPY package.json .

RUN npm install

COPY . ./

ENTRYPOINT ["npm", "start"]