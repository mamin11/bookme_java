CREATE TABLE `users`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `firstname` VARCHAR(255) NOT NULL,
    `lastname` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `image` VARCHAR(255) NULL,
    `user_type` VARCHAR(255) NULL
);
ALTER TABLE
    `users` ADD PRIMARY KEY `users_id_primary`(`id`);
CREATE TABLE `services`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `price` DECIMAL(8, 2) NOT NULL,
    `duration` INT NOT NULL
);
ALTER TABLE
    `services` ADD PRIMARY KEY `services_id_primary`(`id`);
CREATE TABLE `timeslots`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `staff_id` INT NOT NULL,
    `booking_date` DATE NOT NULL,
    `booking_time` TIME NOT NULL
);
ALTER TABLE
    `timeslots` ADD PRIMARY KEY `timeslots_id_primary`(`id`);
CREATE TABLE `staff_services`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `staff_id` INT NOT NULL,
    `service_id` INT NOT NULL
);
ALTER TABLE
    `staff_services` ADD PRIMARY KEY `staff_services_id_primary`(`id`);
CREATE TABLE `booking`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `customer_id` INT NOT NULL,
    `staff_id` INT NOT NULL,
    `service_id` INT NOT NULL,
    `booking_date` DATE NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    `status` INT NOT NULL,
    `media_id` INT NOT NULL
);
ALTER TABLE
    `booking` ADD PRIMARY KEY `booking_id_primary`(`id`);
ALTER TABLE
    `timeslots` ADD CONSTRAINT `timeslots_staff_id_foreign` FOREIGN KEY(`staff_id`) REFERENCES `users`(`id`);
ALTER TABLE
    `staff_services` ADD CONSTRAINT `staff_services_staff_id_foreign` FOREIGN KEY(`staff_id`) REFERENCES `users`(`id`);
ALTER TABLE
    `booking` ADD CONSTRAINT `booking_staff_id_foreign` FOREIGN KEY(`staff_id`) REFERENCES `users`(`id`);
ALTER TABLE
    `booking` ADD CONSTRAINT `booking_customer_id_foreign` FOREIGN KEY(`customer_id`) REFERENCES `users`(`id`);
ALTER TABLE
    `staff_services` ADD CONSTRAINT `staff_services_service_id_foreign` FOREIGN KEY(`service_id`) REFERENCES `services`(`id`);
ALTER TABLE
    `booking` ADD CONSTRAINT `booking_service_id_foreign` FOREIGN KEY(`service_id`) REFERENCES `services`(`id`);