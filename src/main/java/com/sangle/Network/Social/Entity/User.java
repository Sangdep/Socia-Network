package com.sangle.Network.Social.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

}
