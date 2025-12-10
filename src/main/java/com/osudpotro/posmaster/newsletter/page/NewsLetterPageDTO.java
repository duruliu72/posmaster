package com.osudpotro.posmaster.newsletter.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewsLetterPageDTO {
    private Long id;
    private String title;
    private Integer pageNo;
    private Long bannerId; // store Picture ID
    private Long newsLetterId;
}
