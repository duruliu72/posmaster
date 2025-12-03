package com.osudpotro.posmaster.newsletter;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.newsletter.page.NewsLetterPage;
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
public class NewsLetter extends BaseEntity {
    private String name;//Such As May 2025,June 2025
    private Boolean isPublish;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    private Picture banner;
    @OneToMany(mappedBy = "newsLetter", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<NewsLetterPage> newsLetterPages=new ArrayList<>();;
}
