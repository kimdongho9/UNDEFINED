package com.lec.spring.domain.user.oauth;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class KakaoProfile {

    public Long id;
    @JsonProperty("connected_at")
    public String connectedAt;
    public Properties properties;
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean profileNicknameNeedsAgreement;
        public Profile profile;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)  //class에 적용되는 어노테이션, 없으면 무시시켜줌, 이거 없으면에러
        public class Profile {
            public String nickname;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Properties {
        public String nickname;
        public String profile_image;
    }
}


