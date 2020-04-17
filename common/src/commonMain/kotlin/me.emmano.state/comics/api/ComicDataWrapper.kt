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
package me.emmano.state.comics.api

import kotlinx.serialization.Serializable
import me.emmano.state.comics.api.ComicDataContainer


/**
 * 
 * @param code The HTTP status code of the returned result.
 * @param status A string description of the call status.
 * @param copyright The copyright notice for the returned result.
 * @param attributionText The attribution notice for this result.  Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API.
 * @param attributionHTML An HTML representation of the attribution notice for this result.  Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API.
 * @param data The results returned by the call.
 * @param etag A digest value of the content returned by the call.
 */
@Serializable
data class ComicDataWrapper (
    /* The HTTP status code of the returned result. */
    val code: Int? = null,
    /* A string description of the call status. */
    val status: kotlin.String? = null,
    /* The copyright notice for the returned result. */
    val copyright: kotlin.String? = null,
    /* The attribution notice for this result.  Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API. */
    val attributionText: kotlin.String? = null,
    /* An HTML representation of the attribution notice for this result.  Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API. */
    val attributionHTML: kotlin.String? = null,
    /* The results returned by the call. */
    val data: ComicDataContainer? = null,
    /* A digest value of the content returned by the call. */
    val etag: kotlin.String? = null
) {

}
