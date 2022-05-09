# pull official base image
FROM node:13.12.0-alpine

COPY package.json .

RUN npm install

COPY . ./

#RUN npm run build

ENTRYPOINT ["npm", "start"]