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
 * @param available The number of total available series in this list. Will always be greater than or equal to the \"returned\" value.
 * @param returned The number of series returned in this collection (up to 20).
 * @param collectionURI The path to the full list of series in this collection.
 * @param items The list of returned series in this collection.
 */
data class SeriesList (
    /* The number of total available series in this list. Will always be greater than or equal to the \"returned\" value. */
    val available: Int? = null,
    /* The number of series returned in this collection (up to 20). */
    val returned: Int? = null,
    /* The path to the full list of series in this collection. */
    val collectionURI: kotlin.String? = null,
    /* The list of returned series in this collection. */
    val items: List<Series>? = null
) {

}
