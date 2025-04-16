IMAGE_NAME = transaction-manager
VERSION = latest
DOCKERFILE = Dockerfile
CONTAINER_NAME = transaction-manager-container
BUILD_ARGS = --platform linux/amd64

.PHONY: all
all: build

.PHONY: build
build:
	docker build $(BUILD_ARGS) -t $(IMAGE_NAME):$(VERSION) -f $(DOCKERFILE) .

.PHONY: run
run: build
	docker run --rm -d --name $(CONTAINER_NAME) -p 8080:8080 $(IMAGE_NAME):$(VERSION)

.PHONY: stop
stop:
	docker stop $(CONTAINER_NAME) || true
	docker rm $(CONTAINER_NAME) || true

.PHONY: ps
ps:
	docker ps

.PHONY: clean
clean:
	docker rmi $(IMAGE_NAME):$(VERSION) || true

.PHONY: clean-all
clean-all: stop clean

.PHONY: help
help:
	@echo "Makefile Commands:"
	@echo "  make all          - Build the Docker image (default target)"
	@echo "  make build        - Build the Docker image"
	@echo "  make run          - Run the Docker container"
	@echo "  make stop         - Stop and remove the Docker container"
	@echo "  make ps           - Show running Docker containers"
	@echo "  make clean        - Remove Docker image"
	@echo "  make clean-all    - Stop container and remove image"
	@echo "  make help         - Show this help message"
