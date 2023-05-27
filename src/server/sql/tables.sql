CREATE TABLE IF NOT EXISTS tweet (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_tweet_id'),
    author INT,
    context VARCHAR(280),
    attachment INT Array[8],
    retweet smallint default 0,
    likes INT Array[1024],
    favestar bool,
    comments INT Array[1024],
    hashtag INT Array[16],
    postingTime timestamp
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
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_quote_tweet_id'),
    author INT,
    context VARCHAR(280),
    attachment INT array[8],
    retweet smallint default 0,
    likes INT array[1024],
    favestar bool,
    comments INT Array[1024],
    hashtag INT array[16],
    postingTime timestamp,
    quoted_message_id INT
);
CREATE TABLE IF NOT EXISTS poll (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_poll_tweet_id'),
    author INT,
    context VARCHAR(280),
    attachment INT array[8],
    retweet smallint default 0,
    likes INT array[1024],
    favestar bool,
    comments INT Array[1024],
    hashtag INT array[16],
    postingTime timestamp,
    choiceId INT Array[16]
);
CREATE TABLE IF NOT EXISTS choice(
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_choice_id'),
    context VARCHAR(280),
    voters INT Array[1024]
);
CREATE TABLE IF NOT EXISTS Retweet (
    id INT PRIMARY KEY DEFAULT NEXTVAL('seq_retweet_id'),
    author INT,
    retweeted_message_id INT
);