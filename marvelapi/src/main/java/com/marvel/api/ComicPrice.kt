/**
* 
* No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
*
* OpenAPI spec version: Cable
* 
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package com.marvel.api


/**
 * 
 * @param type A description of the price (e.g. print price, digital price).
 * @param price The price (all prices in USD).
 */
data class ComicPrice (
    /* A description of the price (e.g. print price, digital price). */
    val type: kotlin.String? = null,
    /* The price (all prices in USD). */
    val price: kotlin.Float? = null
) {

}
