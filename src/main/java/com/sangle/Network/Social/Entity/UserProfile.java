package com.sangle.Network.Social.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="userProfiles")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    Long id; // trùng với id của User
    String fullName;
    String avatarUrl;
    String bio;
    String gender;
    LocalDate birthDate;


    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;



}
