docker-build-local:
	@docker build . --tag smartglass-backend:dev

docker-up-remote:
	@ssh alizaga@155.210.68.101 "docker run -p 8080:8080 --name smartglass-backend -t adrianliz/smartglass-backend:main"

docker-down-remote:
	@ssh alizaga@155.210.68.101 "docker stop smartglass-backend && docker rm smartglass-backend"