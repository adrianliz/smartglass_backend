build:
	@mvnw clean package

build-image:
	@mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=smartglass-backend

test:
	@mvnw test

docker-up:
	@docker run -p 8080:8080 --name smartglass-backend -t smartglass-backend

docker-up-detached:
	@docker run -p 8080:8080 --name smartglass-backend -t -d smartglass-backend