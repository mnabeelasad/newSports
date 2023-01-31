package com.go.sport.network

import com.go.sport.model.academies.AcademiesModel
import com.go.sport.model.acceptinvite.RespondInviteModel
import com.go.sport.model.bookFacility.BookFacilityModel
import com.go.sport.model.changepassword.ChangePasswordModel
import com.go.sport.model.contactus.ContactUsModel
import com.go.sport.model.creategroup.CreateGroupModel
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody

object APIManager {

    private var disposable: Disposable? = null
    private val retroClient by lazy {
        RetrofitClient.create()
    }

    fun login(
        phoneNumber: String,
        password: String,
        completion: (LoginModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.login(phoneNumber, password)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getCountry(
        completion: (String?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getCountry()
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result.toString(), null)
            }, { error ->
                completion(null, error)
            })
    }

    fun sendOtpToPhoneNumber(
        phoneNumber: String,
        completion: (SendOtpModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.sendOtp(phoneNumber)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun validateOtp(
        phoneNumber: String,
        otp: String,
        completion: (ValidateOtpModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.validateOtp(phoneNumber, otp)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        dateOfBirth: String,
        gender: String,
        completion: (SignUpModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.signUp(
            firstName,
            lastName,
            email,
            phoneNumber,
            password,
            confirmPassword,
            dateOfBirth,
            gender
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun signUpForSocial(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String,
        dateOfBirth: String,
        gender: String,
        completion: (SignUpModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.signUpForSocial(
            firstName,
            lastName,
            email,
            phoneNumber,
            dateOfBirth,
            gender
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getSports(
        headerMap: HashMap<String, String>,
        completion: (GetSportsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getSports(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun selectSports(
        headerMap: HashMap<String, String>,
        sports: String,
        completion: (SelectSportsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.selectSports(headerMap, sports)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun uploadUserImage(
        headerMap: HashMap<String, String>,
        image: RequestBody,
        completion: (UploadImageModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.uploadUserImage(headerMap, image)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getPrivacyPolicy(
        headerMap: HashMap<String, String>,
        completion: (PPandTncModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.privacyPolicy(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getTermsAndConditions(
        headerMap: HashMap<String, String>,
        completion: (PPandTncModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.termsAndConditions(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun changePassword(
        headerMap: HashMap<String, String>,
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        completion: (ChangePasswordModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.changePassword(headerMap, oldPassword, newPassword, confirmNewPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun myProfile(
        headerMap: HashMap<String, String>,
        completion: (GetMyProfileModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.myProfile(headerMap)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun updateProfile(
        headerMap: HashMap<String, String>,
        image: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        gender: RequestBody,
        dateOfBirth: RequestBody,
        completion: (UpdateProfileModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.updateProfile(
            headerMap,
            image,
            firstName,
            lastName,
            gender,
            dateOfBirth
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getFeatures(
        headerMap: HashMap<String, String>,
        completion: (GetFeaturesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getFeatures(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getFacilities(
        headerMap: HashMap<String, String>,
        completion: (GetFacilitiesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getFacilities(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getFacilityData(
        headerMap: HashMap<String, String>,
        facilityId: String,
        completion: (GetFacilityDataModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getFacilityData(headerMap, facilityId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getSlots(
        headerMap: HashMap<String, String>,
        pitchId: String,
        facilityId: String,
        date: String,
        completion: (GetSlotsModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.getSlots(headerMap, pitchId, facilityId, date)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun getSelectedSlotsPrice(
        headerMap: HashMap<String, String>,
        slotId: String,
        completion: (SelectedSlotPriceModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.getSelectedSlotsPrice(headerMap, slotId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun bookFacility(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        date: String,
        facility_id: String,
        pitch_id: String,
        game_fee: String,
        slot_select: String,
        address: String,
        payment: String,
        facility_features: String,
        lat_lng: String,
        completion: (BookFacilityModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.bookFacility(
                headerMap,
                activity_name,
                sports_id,
                date,
                facility_id,
                pitch_id,
                game_fee,
                slot_select,
                address,
                payment,
                facility_features,
                lat_lng,
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun hostActivity(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        facility_title: String,
        facility_features: String,
        additional_info: String,
        skill_level: String,
        total_players: String,
        already_confirmed: String,
        gender: String,
        event_type: String,
        cost_per_play: String,
        payment_type: String,
        slot_select: String,
        date: String,
        facility_id: String,
        age_min: String,
        age_max: String,
        address: String,
        pitch_id: String,
        game_fee: String,
        lat_lng: String,
        completion: (HostActivityForLocationModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.hostActivity(
                headerMap,
                activity_name,
                sports_id,
                facility_title,
                facility_features,
                additional_info,
                skill_level,
                total_players,
                already_confirmed,
                gender,
                event_type,
                cost_per_play,
                payment_type,
                slot_select,
                date,
                facility_id,
                age_min,
                age_max,
                address,
                pitch_id,
                game_fee,
                lat_lng
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun hostActivityConversion(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        facility_title: String,
        facility_features: String,
        additional_info: String,
        skill_level: String,
        total_players: String,
        already_confirmed: String,
        gender: String,
        event_type: String,
        cost_per_play: String,
        payment_type: String,
        slot_select: String,
        date: String,
        facility_id: String,
        age_min: String,
        age_max: String,
        address: String,
        pitch_id: String,
        game_fee: String,
        old_id: Int,
        lat_lng: String,
        completion: (HostActivityModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.hostActivityConversion(
                headerMap,
                activity_name,
                sports_id,
                facility_title,
                facility_features,
                additional_info,
                skill_level,
                total_players,
                already_confirmed,
                gender,
                event_type,
                cost_per_play,
                payment_type,
                slot_select,
                date,
                facility_id,
                age_min,
                age_max,
                address,
                pitch_id,
                game_fee,
                old_id,
                lat_lng
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun hostActivity(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        facility_title: String,
        facility_features: String,
        additional_info: String,
        skill_level: String,
        total_players: String,
        already_confirmed: String,
        gender: String,
        event_type: String,
        cost_per_play: String,
        payment_type: String,
        slot_select: String,
        date: String,
        facility_id: String,
        age_min: String,
        age_max: String,
        address: String,
        pitch_id: String,
        game_fee: String,
        user_invite: ArrayList<String>,
        group_invite: ArrayList<String>,
        lat_lng: String,
        completion: (HostActivityForLocationModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.hostActivityForInvite(
                headerMap,
                activity_name,
                sports_id,
                facility_title,
                facility_features,
                additional_info,
                skill_level,
                total_players,
                already_confirmed,
                gender,
                event_type,
                cost_per_play,
                payment_type,
                slot_select,
                date,
                facility_id,
                age_min,
                age_max,
                address,
                pitch_id,
                game_fee,
                user_invite,
                group_invite,
                lat_lng
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun hostActivityinvitebookconvert(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        facility_title: String,
        facility_features: String,
        additional_info: String,
        skill_level: String,
        total_players: String,
        already_confirmed: String,
        gender: String,
        event_type: String,
        cost_per_play: String,
        payment_type: String,
        slot_select: String,
        date: String,
        facility_id: String,
        age_min: String,
        age_max: String,
        address: String,
        pitch_id: String,
        game_fee: String,
        user_invite: ArrayList<String>,
        group_invite: ArrayList<String>,
        old_id: Int,
        lat_lng: String,
        completion: (HostActivityModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.hostActivityForInviteConvert(
                headerMap,
                activity_name,
                sports_id,
                facility_title,
                facility_features,
                additional_info,
                skill_level,
                total_players,
                already_confirmed,
                gender,
                event_type,
                cost_per_play,
                payment_type,
                slot_select,
                date,
                facility_id,
                age_min,
                age_max,
                address,
                pitch_id,
                game_fee,
                user_invite,
                group_invite,
                old_id,
                lat_lng
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }


    fun hostActivitylocation(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        skill_level: String,
        total_players: String,
        already_confirmed: String,
        gender: String,
        event_type: String,
        cost_per_play: String,
        payment_type: String,
        date: String,
        facility_id: String,
        age_min: String,
        age_max: String,
        address: String,
        pitch: String,
        game_fee: String,
        start_timing_in: String,
        start_timing_out: String,
        pitch_court: String,
        facility_features: String,
        additional_info: String,
        lat_lng: String,

        completion: (HostActivityForLocationModel?, Throwable?) -> Unit
    ) {

        disposable =
            retroClient.hostActivityForLocation(
                headerMap,
                activity_name,
                sports_id,
                skill_level,
                total_players,
                already_confirmed,
                gender,
                event_type,
                cost_per_play,
                payment_type,
                date,
                facility_id,
                age_min,
                age_max,
                address,
                pitch,
                game_fee,
                start_timing_in,
                start_timing_out,
                pitch_court,
                facility_features,
                additional_info,
                lat_lng,
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun hostActivity(
        headerMap: HashMap<String, String>,
        activity_name: String,
        sports_id: String,
        skill_level: String,
        total_players: String,
        already_confirmed: String,
        gender: String,
        event_type: String,
        cost_per_play: String,
        payment_type: String,
        date: String,
        facility_id: String,
        age_min: String,
        age_max: String,
        address: String,
        pitch: String,
        game_fee: String,
        start_timing_in: String,
        start_timing_out: String,
        pitch_court: String,
        user_invite: ArrayList<String>,
        group_invite: ArrayList<String>,
        facility_features: String,
        additional_information: String,
        lat_lng: String,
        completion: (HostActivityForLocationModel?, Throwable?) -> Unit
    ) {
        disposable =
            retroClient.hostActivityForLocationInvite(
                headerMap,
                activity_name,
                sports_id,
                skill_level,
                total_players,
                already_confirmed,
                gender,
                event_type,
                cost_per_play,
                payment_type,
                date,
                facility_id,
                age_min,
                age_max,
                address,
                pitch,
                game_fee,
                start_timing_in,
                start_timing_out,
                pitch_court,
                user_invite,
                group_invite,
                facility_features,
                additional_information,
                lat_lng,
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    completion(result, null)
                }, { error ->
                    completion(null, error)
                })
    }

    fun getLeaderBoard(
        headerMap: HashMap<String, String>,
        completion: (LeaderboardModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getLeaderBoard(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getPlays(
        headerMap: HashMap<String, String>,
        completion: (PlayModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getPlays(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun joinGame(
        headerMap: HashMap<String, String>,
        gameId: String,
        completion: (JoinGameModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.joinGame(headerMap, gameId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun allInvites(
        headerMap: HashMap<String, String>,
        completion: (InvitesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.invites(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun walletAmount(
        headerMap: HashMap<String, String>,
        completion: (WalletAmountModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getWalletAmount(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun walletHistory(
        headerMap: HashMap<String, String>,
        completion: (WalletHistoryModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getWalletHistory(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun transferAmount(
        headerMap: HashMap<String, String>,
        amount: String,
        reason: String,
        receiverId: String,
        completion: (TransferAmountModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.transferWallet(headerMap, amount, reason, receiverId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun rechargeWallet(
        headerMap: HashMap<String, String>,
        amount: String,
        completion: (RechargeWalletModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.rechargeWallet(headerMap, amount)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getUsers(
        headerMap: HashMap<String, String>,
        completion: (GetUsersModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getAllUsers(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun userProfile(
        headerMap: HashMap<String, String>,
        userId: String,
        completion: (GetUserProfileModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getUserProfile(headerMap, userId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun respondInvite(
        headerMap: HashMap<String, String>,
        inviteId: String,
        gameId: String,
        completion: (RespondInviteModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.respondToInvite(headerMap, inviteId, gameId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun leaveGame(
        headerMap: HashMap<String, String>,
        inviteId: String,
        gameId: String,
        completion: (LeaveGameModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.leaveGame(headerMap, inviteId, gameId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }) { error ->
                completion(null, error)
            }
    }

    fun leaveGamePlay(
        headerMap: HashMap<String, String>,
        gameId: String,
        completion: (LeaveGamePlayModel?, Throwable?) -> Unit
    ){
        disposable = retroClient.leaveGamePlay(headerMap, gameId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result,null)
            }) { error ->
                completion(null, error)
            }
    }

    fun getMyGames(
        headerMap: HashMap<String, String>,
        page: Int,
        completion: (MyGamesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getMyGames(headerMap, page.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getGamesOnly(
        headerMap: HashMap<String, String>,
        page: Int,
        completion: (MyGamesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getGamesOnly(headerMap, page.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun pastGames(
        headerMap: HashMap<String, String>,
        page: Int,
        completion: (MyGamesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.pastGames(headerMap, page.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun pastBookings(
        headerMap: HashMap<String, String>,
        page: Int,
        completion: (MyBookingsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.pastBookings(headerMap, page.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getMyBookings(
        headerMap: HashMap<String, String>,
        page: Int,
        completion: (MyBookingsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getMyBookings(headerMap, page.toString())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getAcademies(
        headerMap: HashMap<String, String>,
        completion: (AcademiesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getAcademies(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun facebookSignUp(
        email: String,
        token: String,
        completion: (LoginModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.facebookSignUp(email, token)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun googleSignUp(
        email: String,
        token: String,
        completion: (LoginModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.googleSignUp(email, token)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun gameJoins(
        headerMap: HashMap<String, String>,
        gameId: String,
        completion: (GameJoinsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.gameJoins(headerMap, gameId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun gameInvites(
        headerMap: HashMap<String, String>,
        gameId: String,
        completion: (GameInvitesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.gameInvites(headerMap, gameId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun updateFcmToken(
        headerMap: HashMap<String, String>,
        token: String,
        completion: (UpdateFcmTokenModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.updateFcmToken(headerMap, token)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun fundTransferHistory(
        headerMap: HashMap<String, String>,
        startDate: String,
        endDate: String,
        completion: (FundTransferHistoryModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.fundTransferHistory(headerMap, startDate, endDate)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun gameFilter(
        headerMap: HashMap<String, String>,
        sportsId: String,
        completion: (PlayModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.gameFilter(headerMap, sportsId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun advancedGameFilter(
        headerMap: HashMap<String, String>,
        price: String,
        location: String,
        completion: (PlayModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.advancedGameFilter(headerMap, price, location)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun communityFilter(
        headerMap: HashMap<String, String>,
        gender: String,
        age: String,
        completion: (GetUsersModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.communityFilter(headerMap, gender, age)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun facilityFilter(
        headerMap: HashMap<String, String>,
        sportsId: String,
        price: String,
        facilityName: String,
        completion: (GetFacilitiesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.facilityFilter(headerMap, sportsId, price, facilityName)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun academyFilter(
        headerMap: HashMap<String, String>,
        sportsId: String,
        price: String,
        location: String,
        completion: (AcademiesModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.academyFilter(headerMap, sportsId, price, location)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun sendFundRequest(
        headerMap: HashMap<String, String>,
        amount: String,
        desc: String,
        user: String,
        completion: (SendFundRequestModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.sendFundRequest(headerMap, amount, desc, user)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun receivedFundsRequests(
        headerMap: HashMap<String, String>,
        completion: (ReceivedFundRequestsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.receivedFundsRequests(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun acceptFundRequest(
        headerMap: HashMap<String, String>,
        amount: String,
        reason: String,
        receiverId: String,
        fund_id: Int,
        completion: (TransferAmountModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.acceptFundRequest(headerMap, amount, reason, receiverId, fund_id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun declineFundRequest(
        headerMap: HashMap<String, String>,
        id: String,
        completion: (DeclineFundRequestModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.declineFundRequest(headerMap, id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun listUnjoinedGroups(
        headerMap: HashMap<String, String>,
        completion: (ListUnjoinedModel?, Throwable?) -> Unit
    ) {

        disposable = retroClient.listUnjoinedGroups(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun publicGroups(
        headerMap: HashMap<String, String>,
        completion: (PublicGroupModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.publicGroups(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun listJoinedGroups(
        headerMap: HashMap<String, String>,
        completion: (ListUnjoinedModel?, Throwable?) -> Unit
    ) {

        disposable = retroClient.listJoinedGroups(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun contactUs(
        headerMap: HashMap<String, String>,
        subject: RequestBody,
        name: RequestBody,
        message: RequestBody,
        image: RequestBody?,
        completion: (ContactUsModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.contactUs(headerMap, subject, name, message, image)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun getOffers(
        headerMap: HashMap<String, String>,
        completion: (GetOffersModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getOffers(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }


    fun viewGroup(
        headerMap: HashMap<String, String>,
        group_id: String,
        completion: (ViewGroupModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.viewGroup(headerMap, group_id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun createGroup(
        headerMap: HashMap<String, String>,
        group_name: String,
        members: String,
        completion: (CreateGroupModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.createGroup(headerMap, group_name, members)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }


    fun removeGroupMember(
        headerMap: HashMap<String, String>,
        group_id: String,
        members: String,
        completion: (GenericResponse?, Throwable?) -> Unit
    ) {
        disposable = retroClient.removeGroupMember(headerMap, group_id, members)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }


    fun addGroupMember(
        headerMap: HashMap<String, String>,
        group_id: String,
        members: String,
        completion: (GenericResponse?, Throwable?) -> Unit
    ) {
        disposable = retroClient.addGroupMember(headerMap, group_id, members)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }


    fun sendUserNotification(
        headerMap: HashMap<String, String>,
        userId: String,
        message: String,
        title: String,
        completion: (GenericResponse?, Throwable?) -> Unit
    ) {
        disposable = retroClient.sendUserNotification(headerMap, userId, message, title)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }


    fun sendGroupNotification(
        headerMap: HashMap<String, String>,
        groupId: String,
        message: String,
        title: String,
        completion: (GenericResponse?, Throwable?) -> Unit
    ) {
        disposable = retroClient.sendGroupNotification(headerMap, groupId, message, title)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }

    fun CancelBookings(
        headerMap: HashMap<String, String>,
        id: String,
        completion: (GenericResponse?, Throwable?) -> Unit
    ) {
        disposable = retroClient.CancelBookings(headerMap, id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error -> completion(null, error) })
    }

    fun deleteGroup(
        headerMap: HashMap<String, String>,
        group_id: String,
        completion: (GenericResponse?, Throwable?) -> Unit
    ) {
        disposable = retroClient.removeGroup(headerMap, group_id)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error -> completion(null, error) })
    }

    fun getGroupList(
        headerMap: HashMap<String, String>,
        completion: (GetGroupListModel?, Throwable?) -> Unit
    ) {
        disposable = retroClient.getGroupList(headerMap)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                completion(result, null)
            }, { error ->
                completion(null, error)
            })
    }
}