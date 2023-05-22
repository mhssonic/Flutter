CREATE TABLE IF NOT EXISTS tweet (
	id VARCHAR(16) PRIMARY KEY,
	author VARCHAR(16),
	context VARCHAR(280),
	attachmenst VARCHAR(16) array[8],
	retweet smallserial,
	likes VARCHAR(16) array[1024],
	favestar bool,
	comments VARCHAR(16) Array[1024],
	hashtag VARCHAR(16) array[16],
	postingTime timestamp
);
CREATE TABLE IF NOT EXISTS profile (
	id VARCHAR(16) PRIMARY KEY,
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
 	id VARCHAR(16) PRIMARY KEY,
 	message_id VARCHAR(16) array[4096]
);
CREATE TABLE IF NOT EXISTS users (
 	id VARCHAR(16) PRIMARY KEY,
 	profile_id VARCHAR(16),
 	username VARCHAR(32),
 	password VARCHAR(32),
 	following VARCHAR(16) array[1024],
 	follower VARCHAR(16) array[1024],
 	blocked VARCHAR(16) array[1024]
);
CREATE TABLE IF NOT EXISTS attachment (
    id VARCHAR(16) PRIMARY KEY,
 	type type_file,
 	path VARCHAR(128)
);