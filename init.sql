SELECT 'CREATE DATABASE commerce'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'commerce')\gexec