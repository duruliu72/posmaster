package com.osudpotro.posmaster.newsletter.post;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.newsletter.page.NewsLetterPage;
import com.osudpotro.posmaster.picture.Picture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class NewsLetterPost extends BaseEntity {
    private String title;
    private String sortDesc;
    private String longDesc;
    private String link;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    private Picture image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_letter_page_id")
    @JsonBackReference
    private NewsLetterPage newsLetterPage;
}
