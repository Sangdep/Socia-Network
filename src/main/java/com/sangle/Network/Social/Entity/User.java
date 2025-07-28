package com.sangle.Network.Social.Entity;

import com.sangle.Network.Social.Entity.Chat.ConversationParticipant;
import com.sangle.Network.Social.Entity.Chat.ChatMessage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email;
    String username;
    String password;

    Set<String> role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    UserProfile userProfile ;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Post>posts= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Comment>comments= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConversationParticipant> conversations = new ArrayList<>();

    @OneToMany(mappedBy = "sender" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ChatMessage>messages= new ArrayList<>();

}
