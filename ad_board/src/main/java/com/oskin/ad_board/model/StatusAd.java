package com.oskin.ad_board.model;

public enum StatusAd {
    ACTIVE, // Активный заказа
    HIDDEN, // Скрыт (модерация скрыла товар)
    RESERVED, // Зарезервировано (куплен, но может быть возвращен)
    ARCHIVED, // В архиве (продавец поместил его в архив)
    DRAFT, // Черновик
    MODERATION, // Находится на проверке модерации
    SOLD // Продан
}
