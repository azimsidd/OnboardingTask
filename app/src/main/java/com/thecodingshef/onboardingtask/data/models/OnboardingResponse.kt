package com.thecodingshef.onboardingtask.data.models

import com.google.gson.annotations.SerializedName

data class OnboardingResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: OnboardingData? = null
)

data class OnboardingData(
    @SerializedName("manualBuyEducationData") val manualBuyEducationData: ManualBuyEducationData? = null
)

data class ManualBuyEducationData(
    @SerializedName("toolBarText") val toolBarText: String? = null,
    @SerializedName("introTitle") val introTitle: String? = null,
    @SerializedName("introSubtitle") val introSubtitle: String? = null,
    @SerializedName("educationCardList") val educationCardList: List<EducationCard>? = null,
    @SerializedName("saveButtonCta") val saveButtonCta: SaveButtonCta? = null,
    @SerializedName("ctaLottie") val ctaLottie: String? = null,
    @SerializedName("screenType") val screenType: String? = null,
    @SerializedName("cohort") val cohort: String? = null,
    @SerializedName("combination") val combination: String? = null,
    @SerializedName("collapseCardTiltInterval") val collapseCardTiltInterval: Int? = null,
    @SerializedName("collapseExpandIntroInterval") val collapseExpandIntroInterval: Int = 0,
    @SerializedName("bottomToCenterTranslationInterval") val bottomToCenterTranslationInterval: Int? = null,
    @SerializedName("expandCardStayInterval") val expandCardStayInterval: Int = 0,
    @SerializedName("seenCount") val seenCount: Int?? = null,
    @SerializedName("actionText") val actionText: String? = null,
    @SerializedName("shouldShowOnLandingPage") val shouldShowOnLandingPage: Boolean? = null,
    @SerializedName("toolBarIcon") val toolBarIcon: String? = null,
    @SerializedName("introSubtitleIcon") val introSubtitleIcon: String? = null,
    @SerializedName("shouldShowBeforeNavigating") val shouldShowBeforeNavigating: Boolean? = null
)

data class EducationCard(
    @SerializedName("image") val image: String,
    @SerializedName("collapsedStateText") val collapsedStateText: String? = null,
    @SerializedName("expandStateText") val expandStateText: String? = null,
    @SerializedName("backGroundColor") val backgroundColor: String? = null,
    @SerializedName("strokeStartColor") val strokeStartColor: String? = null,
    @SerializedName("strokeEndColor") val strokeEndColor: String? = null,
    @SerializedName("startGradient") val startGradient: String? = null,
    @SerializedName("endGradient") val endGradient: String? = null
)

data class SaveButtonCta(
    @SerializedName("text") val text: String? = null,
    @SerializedName("deeplink") val deeplink: String? = null,
    @SerializedName("backgroundColor") val backgroundColor: String? = null,
    @SerializedName("textColor") val textColor: String? = null,
    @SerializedName("strokeColor") val strokeColor: String? = null,
    @SerializedName("icon") val icon: String? = null,
    @SerializedName("order") val order: Int? = null
)
