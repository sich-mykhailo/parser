package com.parser.parser.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OlxUrls {

    public static final String BASE ="https://www.olx.ua/";
    public static final String INDEX_D = "d/uk/";

    /**CATEGORIES
     * ***************************************************
     */
    public static final String AAL_CATEGORY = BASE + INDEX_D + "list/q-";
    public static final String HELP = BASE + INDEX_D + "dopomoga/q-";
    public static final String CHILDREN = BASE + INDEX_D +"detskiy-mir/q-";
    public static final String HOUSE = BASE + INDEX_D + "nedvizhimost/q-";
    public static final String CAR = BASE + INDEX_D + "transport/q-";
    public static final String DETAILS = BASE + INDEX_D + "zapchasti-dlya-transporta/q-";
    public static final String WORK = BASE + INDEX_D + "rabota/q-";
    public static final String PETS = BASE + INDEX_D + "zhivotnye/q-";
    public static final String GARDEN =  BASE + INDEX_D +"dom-i-sad/q-";
    public static final String ELECTRONIC = BASE + INDEX_D + "elektronika/q-";
    public static final String STYLE = BASE + INDEX_D + "moda-i-stil/q-";
    public static final String RELAX = BASE + INDEX_D + "hobbi-otdyh-i-sport/q-";

    public static final String BUSINESS = BASE + INDEX_D + "uslugi/q-";


    /**PARAMETERS
     ****************************************************
     */
    public static  final String START = "?";
    public static  final String AND = "&";
    public static  final String CURRENCY_UAH = "currency=UAH";
    public static  final String CURRENCY_USD = "currency=USD";
    public static  final String CURRENCY_EUR = "currency=EUR";
    public static  final String COURIER = "courier=1";
    public static final String ONLY_PHOTOS = "search%5Bphotos%5D=1";
}
