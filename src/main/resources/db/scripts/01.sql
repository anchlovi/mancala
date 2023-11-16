CREATE TABLE games
(
    id      UUID PRIMARY KEY,
    version INTEGER NOT NULL DEFAULT 0,
    data    JSONB NOT NULL
);

CREATE INDEX idx_games_id_and_version ON games(id, version);