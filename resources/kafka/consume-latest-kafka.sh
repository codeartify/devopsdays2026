#!/usr/bin/env bash

set -e

TOPIC=${1:-axon.events}
GROUP=${2:-debug-latest-$(date +%s)}

echo "Consuming latest events..."
echo "Topic: $TOPIC"
echo "Group: $GROUP"
echo "----------------------------------------"

docker exec -it local-kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic "$TOPIC" \
  --group "$GROUP" \
  --consumer-property auto.offset.reset=latest
