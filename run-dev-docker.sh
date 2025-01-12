docker compose --env-file .env -f ./dev/compose.yml down \
  && docker compose --env-file .env -f ./dev/compose.yml up -d