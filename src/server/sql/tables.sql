CREATE TABLE IF NOT EXISTS tweet (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_tweet_id'),
    author INT,
    context VARCHAR(280),
    attachment INT Array[8],
    retweet smallint default 0,
    likes INT Array[1024],
    favestar bool,
    comments VARCHAR(50) Array[1024],
    hashtag VARCHAR(50) Array[16],
    postingTime timestamp
);
CREATE TABLE IF NOT EXISTS comment (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_tweet_id') + 1,
    author INT,
    context VARCHAR(280),
    attachment INT Array[8],
    retweet smallint default 0,
    likes INT Array[1024],
    favestar bool,
    comments VARCHAR(50) Array[1024],
    hashtag VARCHAR(50) Array[16],
    postingTime timestamp,
    reply INT
    );
CREATE TABLE IF NOT EXISTS profile (
    id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(64),
    phone_number VARCHAR(13),
    country VARCHAR(3),
    birthdate date,
    last_edit timestamp,
    bio VARCHAR(160),
    avatar VARCHAR(128),
    header VARCHAR(128)
);
CREATE TABLE IF NOT EXISTS chat_box (
    id INT PRIMARY KEY,
    message_id INT array[4096]
);
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_users_id'),
    username VARCHAR(32),
    password VARCHAR(32),
    following INT array[1024],
    follower INT array[1024],
    blocked INT array[1024]
);
CREATE TABLE IF NOT EXISTS attachment(
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_attachment_id'),
    type type_file,
    path VARCHAR(128)
);
CREATE TABLE IF NOT EXISTS quote (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_tweet_id') + 4,
    author INT,
    context VARCHAR(280),
    attachment INT array[8],
    retweet smallint default 0,
    likes INT array[1024],
    favestar bool,
    comments VARCHAR(50) Array[1024],
    hashtag VARCHAR(50) array[16],
    postingTime timestamp,
    quoted_message_id INT
);
CREATE TABLE IF NOT EXISTS poll (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_tweet_id') + 3,
    author INT,
    context VARCHAR(280),
    attachment INT array[8],
    retweet smallint default 0,
    likes INT array[1024],
    favestar bool,
    comments VARCHAR(50) Array[1024],
    hashtag VARCHAR(50) array[16],
    postingTime timestamp,
    choice INT Array[16]
);
CREATE TABLE IF NOT EXISTS choice(
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_choice_id'),
    context VARCHAR(280),
    voters INT Array[1024]
);
CREATE TABLE IF NOT EXISTS retweet (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_tweet_id') + 2,
    author INT,
    retweeted_message_id INT
);
DO
$$
BEGIN
IF NOT EXISTS (
    SELECT FROM
        pg_tables
    WHERE
        schemaname = 'public' AND
        tablename  = 'secret_key'
    )
Then
CREATE TABLE secret_key (
    id VARCHAR(50),
    value VARCHAR(200)
);
INSERT INTO secret_key (id, value) values('token', 'ba esm ramz pashmak');
INSERT INTO secret_key (id, value) values('key_store', 'pashmak');

end if;

END;
$$;