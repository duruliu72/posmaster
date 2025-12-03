package com.osudpotro.posmaster.newsletter.page;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.newsletter.NewsLetter;
import com.osudpotro.posmaster.newsletter.post.NewsLetterPost;
import com.osudpotro.posmaster.picture.Picture;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class NewsLetterPage extends BaseEntity {
    private String title;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    private Picture banner;
    private Integer pageNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_letter_id")
    @JsonBackReference
    private NewsLetter newsLetter;
    @OneToMany(mappedBy = "newsLetterPage", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<NewsLetterPost> posts=new ArrayList<>();
}
