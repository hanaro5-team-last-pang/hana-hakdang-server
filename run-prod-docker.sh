docker compose --env-file .env.prod -f ./deploy/compose.yml down --rmi local \
  && docker compose --env-file .env.prod -f ./deploy/compose.yml up -d