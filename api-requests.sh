#!/bin/bash -eux
RL=50
for ((i=1;i<=RL;i++)); do
	echo "Calling API with Rate Limiter"
 	curl http://localhost:8080/v1/api/rl
	echo "\n"
done

echo "----------"

CB=30
for ((i=1;i<=CB;i++)); do
	echo "Calling API with Circuit Breaker"
	curl http://localhost:8080/v1/api/cb
	echo "\n"
done
