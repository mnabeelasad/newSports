package com.go.sport.network

import com.go.sport.constants.Constants
import com.go.sport.model.academies.AcademiesModel
import com.go.sport.model.acceptinvite.RespondInviteModel
import com.go.sport.model.bookFacility.BookFacilityModel
import com.go.sport.model.changepassword.ChangePasswordModel
import com.go.sport.model.contactus.ContactUsModel
import com.go.sport.model.creategroup.CreateGroupModel
import com.go.sport.model.deletegroup.DeleteGroupModel
import com.go.sport.model.fundrequest.decline.DeclineFundRequestModel
import com.go.sport.model.fundrequest.received.ReceivedFundRequestsModel
import com.go.sport.model.fundrequest.send.SendFundRequestModel
import com.go.sport.model.fundtransferhistory.FundTransferHistoryModel
import com.go.sport.model.gameinvites.GameInvitesModel
import com.go.sport.model.gamejoins.GameJoinsModel
import com.go.sport.model.generic.GenericResponse
import com.go.sport.model.getfacilities.GetFacilitiesModel
import com.go.sport.model.getfacilitydata.GetFacilityDataModel
import com.go.sport.model.getfeatures.GetFeaturesModel
import com.go.sport.model.getgrouplist.GetGroupListModel
import com.go.sport.model.getmyprofile.GetMyProfileModel
import com.go.sport.model.getoffers.GetOffersModel
import com.go.sport.model.getslots.GetSlotsModel
import com.go.sport.model.getsports.GetSportsModel
import com.go.sport.model.getusers.GetUsersModel
import com.go.sport.model.hostactivity.HostActivityModel
import com.go.sport.model.hostactivity.hostForLocation.HostActivityForLocationModel
import com.go.sport.model.invites.InvitesModel
import com.go.sport.model.leaderboardnew.LeaderboardModel
import com.go.sport.model.leavegame.LeaveGameModel
import com.go.sport.model.leavegameplay.LeaveGamePlayModel
import com.go.sport.model.login.LoginModel
import com.go.sport.model.mybookings.MyBookingsModel
import com.go.sport.model.mygames.MyGamesModel
import com.go.sport.model.play.PlayModel
import com.go.sport.model.play.join.JoinGameModel
import com.go.sport.model.ppntnc.PPandTncModel
import com.go.sport.model.publicgroups.PublicGroupModel
import com.go.sport.model.selectedslotprice.SelectedSlotPriceModel
import com.go.sport.model.selectsports.SelectSportsModel
import com.go.sport.model.send.SendOtpModel
import com.go.sport.model.signup.SignUpModel
import com.go.sport.model.unjoinedgroups.ListUnjoinedModel
import com.go.sport.model.updatefcmtoken.UpdateFcmTokenModel
import com.go.sport.model.updateprofile.UpdateProfileModel
import com.go.sport.model.uploadimage.UploadImageModel
import com.go.sport.model.userprofile.GetUserProfileModel
import com.go.sport.model.validateotp.ValidateOtpModel
import com.go.sport.model.viewgroup.ViewGroupModel
import com.go.sport.model.wallet.RechargeWalletModel
import com.go.sport.model.wallet.get.WalletAmountModel
import com.go.sport.model.wallet.history.WalletHistoryModel
import com.go.sport.model.wallet.transfer.TransferAmountModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @POST(Constants.LOGIN)
    fun login(
        @Field("phone_number") phone_number: String,
        @Field("password") password: String
    ): Observable<LoginModel>

    @FormUrlEncoded
    @POST(Constants.SEND)
    fun sendOtp(
        @Field("phone_number") phone_number: String
    ): Observable<SendOtpModel>

    @FormUrlEncoded
    @POST(Constants.VALIDATE_OTP)
    fun validateOtp(
        @Field("phone_number") phone_number: String,
        @Field("otp") otp: String
    ): Observable<ValidateOtpModel>

    @FormUrlEncoded
    @POST(Constants.SIGN_UP)
    fun signUp(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("phone_number") phone_number: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("gender") gender: String
    ): Observable<SignUpModel>

    @FormUrlEncoded
    @POST(Constants.SIGN_UP)
    fun signUpForSocial(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("phone_number") phone_number: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("gender") gender: String,
        @Field("social_login") social_login: Boolean = true
    ): Observable<SignUpModel>

    @GET(Constants.GET_SPORTS)
    fun getSports(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetSportsModel>

    @Headers("Content-Type: application/json")
    @GET("http://ipinfo.io/country")
    fun getCountry(
    ): Observable<Any>

    @FormUrlEncoded
    @POST(Constants.SELECT_SPORTS)
    fun selectSports(
        @HeaderMap headers: Map<String, String>,
        @Field("sports") sports: String
    ): Observable<SelectSportsModel>

    @Multipart
    @POST(Constants.UPLOAD_USER_IMAGE)
    fun uploadUserImage(
        @HeaderMap headers: Map<String, String>,
        @Part("profile_image\"; filename=\"user_image.png") image: RequestBody?
    ): Observable<UploadImageModel>


    @GET(Constants.PRIVACY_POLICY)
    fun privacyPolicy(
        @HeaderMap headers: Map<String, String>
    ): Observable<PPandTncModel>

    @GET(Constants.TERMS_AND_CONDITIONS)
    fun termsAndConditions(
        @HeaderMap headers: Map<String, String>
    ): Observable<PPandTncModel>

    @FormUrlEncoded
    @POST(Constants.CHANGE_PASSWORD)
    fun changePassword(
        @HeaderMap headers: Map<String, String>,
        @Field("old_password") old_password: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Observable<ChangePasswordModel>

    @GET(Constants.MY_PROFILE)
    fun myProfile(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetMyProfileModel>

    @Multipart
    @POST(Constants.UPDATE_PROFILE)
    fun updateProfile(
        @HeaderMap headers: Map<String, String>,
        @Part("profile_image\"; filename=\"user_image.png") image: RequestBody?,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("date_of_birth") date_of_birth: RequestBody
    ): Observable<UpdateProfileModel>

    @GET(Constants.GET_FEATURES)
    fun getFeatures(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetFeaturesModel>

    @GET(Constants.GET_FACILITIES)
    fun getFacilities(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetFacilitiesModel>

    @FormUrlEncoded
    @POST(Constants.GET_FACILITIES_DATA)
    fun getFacilityData(
        @HeaderMap headers: Map<String, String>,
        @Field("id") id: String
    ): Observable<GetFacilityDataModel>

    @FormUrlEncoded
    @POST(Constants.GET_SLOTS)
    fun getSlots(
        @HeaderMap headers: Map<String, String>,
        @Field("pitch_id") pitch_id: String,
        @Field("facility_id") facility_id: String,
        @Field("date") date: String
    ): Observable<GetSlotsModel>

    @FormUrlEncoded
    @POST(Constants.SELECTED_SLOTS_PRICE)
    fun getSelectedSlotsPrice(
        @HeaderMap headers: Map<String, String>,
        @Field("slot_id") slot_id: String
    ): Observable<SelectedSlotPriceModel>

    @FormUrlEncoded
    @POST(Constants.BOOK_FACILITY)
    fun bookFacility(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("pitch_id") pitch_id: String,
        @Field("game_fee") game_fee: String,
        @Field("slot_select") slot_select: String,
        @Field("address") address: String,
        @Field("payment_type") payment: String,
        @Field("facility_features") facility_features: String,
        @Field("lat_lng") lat_lng: String,
    ): Observable<BookFacilityModel>

    @FormUrlEncoded
    @POST(Constants.HOST_ACTIVITY)
    fun hostActivity(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("facility_title") facility_title: String,
        @Field("facility_features") facility_features: String,
        @Field("additional_info") additional_info: String,
        @Field("skill_level") skill_level: String,
        @Field("total_players") total_players: String,
        @Field("already_confirmed") already_confirmed: String,
        @Field("gender") gender: String,
        @Field("event_type") event_type: String,
        @Field("cost_per_play") cost_per_play: String,
        @Field("payment_type") payment_type: String,
        @Field("slot_select") slot_select: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("age_min") age_min: String,
        @Field("age_max") age_max: String,
        @Field("address") address: String,
        @Field("pitch_id") pitch_id: String,
        @Field("game_fee") game_fee: String,
        @Field("lat_lng") lat_lng: String,
    ): Observable<HostActivityForLocationModel>

    @FormUrlEncoded
    @POST(Constants.HOST_ACTIVITY)
    fun hostActivityConversion(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("facility_title") facility_title: String,
        @Field("facility_features") facility_features: String,
        @Field("additional_info") additional_info: String,
        @Field("skill_level") skill_level: String,
        @Field("total_players") total_players: String,
        @Field("already_confirmed") already_confirmed: String,
        @Field("gender") gender: String,
        @Field("event_type") event_type: String,
        @Field("cost_per_play") cost_per_play: String,
        @Field("payment_type") payment_type: String,
        @Field("slot_select") slot_select: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("age_min") age_min: String,
        @Field("age_max") age_max: String,
        @Field("address") address: String,
        @Field("pitch_id") pitch_id: String,
        @Field("game_fee") game_fee: String,
        @Field("old_id") old_id: Int,
        @Field("lat_lng") lat_lng: String,

        ): Observable<HostActivityModel>

    @FormUrlEncoded
    @POST(Constants.HOST_ACTIVITY)
    fun hostActivityForInvite(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("facility_title") facility_title: String,
        @Field("facility_features") facility_features: String,
        @Field("additional_info") additional_info: String,
        @Field("skill_level") skill_level: String,
        @Field("total_players") total_players: String,
        @Field("already_confirmed") already_confirmed: String,
        @Field("gender") gender: String,
        @Field("event_type") event_type: String,
        @Field("cost_per_play") cost_per_play: String,
        @Field("payment_type") payment_type: String,
        @Field("slot_select") slot_select: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("age_min") age_min: String,
        @Field("age_max") age_max: String,
        @Field("address") address: String,
        @Field("pitch_id") pitch_id: String,
        @Field("game_fee") game_fee: String,
        @Field("user_invite") user_invite: ArrayList<String>,
        @Field("group_invite") group_invite: ArrayList<String>,
        @Field("lat_lng") lat_lng: String,
    ): Observable<HostActivityForLocationModel>

    @FormUrlEncoded
    @POST(Constants.HOST_ACTIVITY)
    fun hostActivityForInviteConvert(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("facility_title") facility_title: String,
        @Field("facility_features") facility_features: String,
        @Field("additional_info") additional_info: String,
        @Field("skill_level") skill_level: String,
        @Field("total_players") total_players: String,
        @Field("already_confirmed") already_confirmed: String,
        @Field("gender") gender: String,
        @Field("event_type") event_type: String,
        @Field("cost_per_play") cost_per_play: String,
        @Field("payment_type") payment_type: String,
        @Field("slot_select") slot_select: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("age_min") age_min: String,
        @Field("age_max") age_max: String,
        @Field("address") address: String,
        @Field("pitch_id") pitch_id: String,
        @Field("game_fee") game_fee: String,
        @Field("user_invite") user_invite: ArrayList<String>,
        @Field("group_invite") group_invite: ArrayList<String>,
        @Field("old_id") old_id: Int,
        @Field("lat_lng") lat_lng: String,

        ): Observable<HostActivityModel>

    @FormUrlEncoded
    @POST(Constants.HOST_ACTIVITY)
    fun hostActivityForLocation(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("skill_level") skill_level: String,
        @Field("total_players") total_players: String,
        @Field("already_confirmed") already_confirmed: String,
        @Field("gender") gender: String,
        @Field("event_type") event_type: String,
        @Field("cost_per_play") cost_per_play: String,
        @Field("payment_type") payment_type: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("age_min") age_min: String,
        @Field("age_max") age_max: String,
        @Field("address") address: String,
        @Field("pitch") pitch: String,
        @Field("game_fee") game_fee: String,
        @Field("start_timing_out") start_timing_out: String,
        @Field("end_timing_out") end_timing_out: String,
        @Field("pitch_court") pitch_court: String,
        @Field("facility_features") facility_features: String,
        @Field("additional_info") additional_info: String,
        @Field("lat_lng") lat_lng: String,
    ): Observable<HostActivityForLocationModel>

    @FormUrlEncoded
    @POST(Constants.HOST_ACTIVITY)
    fun hostActivityForLocationInvite(
        @HeaderMap headers: Map<String, String>,
        @Field("activity_name") activity_name: String,
        @Field("sports_id") sports_id: String,
        @Field("skill_level") skill_level: String,
        @Field("total_players") total_players: String,
        @Field("already_confirmed") already_confirmed: String,
        @Field("gender") gender: String,
        @Field("event_type") event_type: String,
        @Field("cost_per_play") cost_per_play: String,
        @Field("payment_type") payment_type: String,
        @Field("date") date: String,
        @Field("facility_id") facility_id: String,
        @Field("age_min") age_min: String,
        @Field("age_max") age_max: String,
        @Field("address") address: String,
        @Field("pitch") pitch: String,
        @Field("game_fee") game_fee: String,
        @Field("start_timing_out") start_timing_out: String,
        @Field("end_timing_out") end_timing_out: String,
        @Field("pitch_court") pitch_court: String,
        @Field("user_invite") user_invite: ArrayList<String>,
        @Field("group_invite") group_invite: ArrayList<String>,
        @Field("facility_features") facility_features: String,
        @Field("additional_information") additional_information: String,
        @Field("lat_lng") lat_lng: String,
    ): Observable<HostActivityForLocationModel>

    @GET(Constants.LEADER_BOARD)
    fun getLeaderBoard(
        @HeaderMap headers: Map<String, String>
    ): Observable<LeaderboardModel>

    @GET(Constants.PLAY)
    fun getPlays(
        @HeaderMap headers: Map<String, String>,
        ): Observable<PlayModel>

    @FormUrlEncoded
    @POST(Constants.JOIN_GAME)
    fun joinGame(
        @HeaderMap headers: Map<String, String>,
        @Field("game_id") game_id: String
    ): Observable<JoinGameModel>

    @GET(Constants.INVITES)
    fun invites(
        @HeaderMap headers: Map<String, String>
    ): Observable<InvitesModel>

    @GET(Constants.WALLET_AMOUNT)
    fun getWalletAmount(
        @HeaderMap headers: Map<String, String>
    ): Observable<WalletAmountModel>

    @GET(Constants.WALLET_HISTORY)
    fun getWalletHistory(
        @HeaderMap headers: Map<String, String>
    ): Observable<WalletHistoryModel>

    @FormUrlEncoded
    @POST(Constants.TRANSFER_WALLET)
    fun transferWallet(
        @HeaderMap headers: Map<String, String>,
        @Field("amount") amount: String,
        @Field("reason") reason: String,
        @Field("reciever_id") reciever_id: String
    ): Observable<TransferAmountModel>

    @FormUrlEncoded
    @POST(Constants.RECHARGE_WALLET)
    fun rechargeWallet(
        @HeaderMap headers: Map<String, String>,
        @Field("amount") amount: String
    ): Observable<RechargeWalletModel>

    @GET(Constants.GET_USERS)
    fun getAllUsers(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetUsersModel>

    @FormUrlEncoded
    @POST(Constants.USER_PROFILE)
    fun getUserProfile(
        @HeaderMap headers: Map<String, String>,
        @Field("user_id") user_id: String
    ): Observable<GetUserProfileModel>

    @FormUrlEncoded
    @POST(Constants.RESPOND_INVITE)
    fun respondToInvite(
        @HeaderMap headers: Map<String, String>,
        @Field("invite_id") invite_id: String,
        @Field("game_id") game_id: String,
        @Field("status") status: String = "accept"
    ): Observable<RespondInviteModel>


    @FormUrlEncoded
    @POST(Constants.LEAVE_GAME)
    fun leaveGame(
        @HeaderMap headers: Map<String, String>,
        @Field("invite_id") invite_id: String,
        @Field("game_id") game_id: String
    ): Observable<LeaveGameModel>


    @FormUrlEncoded
    @POST(Constants.LEAVE_GAME_PLAY)
    fun leaveGamePlay(
        @HeaderMap headers: Map<String, String>,
        @Field("game_id") game_id: String
    ): Observable<LeaveGamePlayModel>


    @GET(Constants.MY_GAMES)
    fun getMyGames(
        @HeaderMap headers: Map<String, String>,
        @Query("page") memberId: String
    ): Observable<MyGamesModel>

    @GET(Constants.MY_BOOKINGS)
    fun getMyBookings(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: String
    ): Observable<MyBookingsModel>

    @GET(Constants.GET_ACADMIES)
    fun getAcademies(
        @HeaderMap headers: Map<String, String>
    ): Observable<AcademiesModel>

    @FormUrlEncoded
    @POST(Constants.FACEBOOK_SIGN_UP)
    fun facebookSignUp(
        @Field("email") email: String,
        @Field("token") token: String
    ): Observable<LoginModel>

    @FormUrlEncoded
    @POST(Constants.GOOGLE_SIGN_UP)
    fun googleSignUp(
        @Field("email") email: String,
        @Field("token") token: String
    ): Observable<LoginModel>

    @FormUrlEncoded
    @POST(Constants.GAME_JOINS)
    fun gameJoins(
        @HeaderMap headers: Map<String, String>,
        @Field("game_id") game_id: String
    ): Observable<GameJoinsModel>

    @FormUrlEncoded
    @POST(Constants.GAME_INVITES)
    fun gameInvites(
        @HeaderMap headers: Map<String, String>,
        @Field("game_id") game_id: String
    ): Observable<GameInvitesModel>

    @FormUrlEncoded
    @POST(Constants.UPDATE_FCM_TOKEN)
    fun updateFcmToken(
        @HeaderMap headers: Map<String, String>,
        @Field("fcm_token") fcm_token: String,
        @Field("user_agent") user_agent: String = "android"
    ): Observable<UpdateFcmTokenModel>

    @FormUrlEncoded
    @POST(Constants.FUND_HISTORY_FILTER)
    fun fundTransferHistory(
        @HeaderMap headers: Map<String, String>,
        @Field("start_date") start_date: String,
        @Field("end_date") end_date: String
    ): Observable<FundTransferHistoryModel>

    @FormUrlEncoded
    @POST(Constants.GAME_FILTER)
    fun gameFilter(
        @HeaderMap headers: Map<String, String>,
        @Field("sports_id") sports_id: String
    ): Observable<PlayModel>

    @FormUrlEncoded
    @POST(Constants.ADVANCED_GAME_FILTER)
    fun advancedGameFilter(
        @HeaderMap headers: Map<String, String>,
        @Field("price") price: String,
        @Field("location") location: String
    ): Observable<PlayModel>

    @FormUrlEncoded
    @POST(Constants.COMMUNITY_FILTER)
    fun communityFilter(
        @HeaderMap headers: Map<String, String>,
        @Field("gender") gender: String,
        @Field("age") age: String
    ): Observable<GetUsersModel>

    @FormUrlEncoded
    @POST(Constants.FACILITY_FILTER)
    fun facilityFilter(
        @HeaderMap headers: Map<String, String>,
        @Field("sports_id") sports_id: String,
        @Field("price") price: String,
        @Field("facility_name") facility_name: String
    ): Observable<GetFacilitiesModel>

    @FormUrlEncoded
    @POST(Constants.ACADEMY_FILTER)
    fun academyFilter(
        @HeaderMap headers: Map<String, String>,
        @Field("sports_id") sports_id: String,
        @Field("price") price: String,
        @Field("location") location: String
    ): Observable<AcademiesModel>

    @FormUrlEncoded
    @POST(Constants.REQUEST_FUND)
    fun sendFundRequest(
        @HeaderMap headers: Map<String, String>,
        @Field("amount") amount: String,
        @Field("description") description: String,
        @Field("user") user: String
    ): Observable<SendFundRequestModel>

    @GET(Constants.RECIEVED_REQS)
    fun receivedFundsRequests(
        @HeaderMap headers: Map<String, String>
    ): Observable<ReceivedFundRequestsModel>

    @FormUrlEncoded
    @POST(Constants.ACCEPT_RECEIVED_REQ)
    fun acceptFundRequest(
        @HeaderMap headers: Map<String, String>,
        @Field("amount") amount: String,
        @Field("reason") reason: String,
        @Field("reciever_id") reciever_id: String,
        @Field("fund_id") fund_id: Int
    ): Observable<TransferAmountModel>

    @FormUrlEncoded
    @POST(Constants.DELETE_RECIEVED_REQS)
    fun declineFundRequest(
        @HeaderMap headers: Map<String, String>,
        @Field("id") id: String
    ): Observable<DeclineFundRequestModel>


    @GET(Constants.JOINED_GROUPS)
    fun listJoinedGroups(
        @HeaderMap headers: Map<String, String>
    ): Observable<ListUnjoinedModel>

    @FormUrlEncoded
    @POST(Constants.VIEW_GROUP)
    fun viewGroup(
        @HeaderMap headers: Map<String, String>,
        @Field("group_id") group_id: String
    ): Observable<ViewGroupModel>

    @FormUrlEncoded
    @POST(Constants.CREATE_GROUP)
    fun createGroup(
        @HeaderMap headers: Map<String, String>,
        @Field("group_name") group_name: String,
        @Field("members") members: String
    ): Observable<CreateGroupModel>

    @FormUrlEncoded
    @POST(Constants.REMOVE_GROUP_MEMBER)
    fun removeGroupMember(
        @HeaderMap headers: Map<String, String>,
        @Field("group_id") group_name: String,
        @Field("members") members: String
    ): Observable<GenericResponse>

    @FormUrlEncoded
    @POST(Constants.ADD_GROUP_MEMBER)
    fun addGroupMember(
        @HeaderMap headers: Map<String, String>,
        @Field("group_id") group_name: String,
        @Field("members") members: String
    ): Observable<GenericResponse>

    @FormUrlEncoded
    @POST(Constants.SEND_USER_NOTIFICATION)
    fun sendUserNotification(
        @HeaderMap headers: Map<String, String>,
        @Field("users") user_id: String,
        @Field("message") message: String,
        @Field("title") title: String
    ): Observable<GenericResponse>

    @FormUrlEncoded
    @POST(Constants.SEND_GROUP_NOTIFICATION)
    fun sendGroupNotification(
        @HeaderMap headers: Map<String, String>,
        @Field("group") user_id: String,
        @Field("message") message: String,
        @Field("title") title: String
    ): Observable<GenericResponse>


    @Multipart
    @POST(Constants.CONTACT_US)
    fun contactUs(
        @HeaderMap headers: Map<String, String>,
        @Part("subject") subject: RequestBody,
        @Part("name") name: RequestBody,
        @Part("message") message: RequestBody,
        @Part("image\"; filename=\"contact_us_img.png") image: RequestBody?
    ): Observable<ContactUsModel>


    @GET(Constants.GET_OFFERS)
    fun getOffers(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetOffersModel>

    @GET(Constants.JOINED_GROUPS)
    fun getGroupList(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetGroupListModel>

    @GET(Constants.GET_OFFERS)
    fun getUserList(
        @HeaderMap headers: Map<String, String>
    ): Observable<GetOffersModel>

    @GET(Constants.GAMES_ONLY)
    fun getGamesOnly(
        @HeaderMap headers: Map<String, String>,
        @Query("page") memberId: String
    ): Observable<MyGamesModel>

    @FormUrlEncoded
    @POST(Constants.CANCEL_BOOKINGS)
    fun CancelBookings(
        @HeaderMap headers: Map<String, String>,
        @Field("id") id: String,
    ): Observable<GenericResponse>

    @FormUrlEncoded
    @POST(Constants.REMOVE_GROUP)
    fun removeGroup(
        @HeaderMap headers: Map<String, String>,
        @Field("group_id") group_id: String,
    ): Observable<GenericResponse>

    @GET(Constants.PAST_GAMES)
    fun pastGames(
        @HeaderMap headers: Map<String, String>,
        @Query("page") memberId: String
    ): Observable<MyGamesModel>

    @GET(Constants.PAST_BOOKINGS)
    fun pastBookings(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: String
    ): Observable<MyBookingsModel>

    @GET(Constants.PUBLIC_GROUPS)
    fun publicGroups(
        @HeaderMap header: Map<String, String>
    ): Observable<PublicGroupModel>

    @GET(Constants.LIST_UNJOINED)
    fun listUnjoinedGroups(
        @HeaderMap headers: Map<String, String>
    ): Observable<ListUnjoinedModel>

}
