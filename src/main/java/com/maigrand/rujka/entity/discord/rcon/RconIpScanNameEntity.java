package com.maigrand.rujka.entity.discord.rcon;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rcon_ipscan_name")
@NoArgsConstructor
@Data
public class RconIpScanNameEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rcon_ipscan_ip_name",
            joinColumns = @JoinColumn(name = "rcon_ipscan_name_id"),
            inverseJoinColumns = @JoinColumn(name = "rcon_ipscan_ip_id")
    )
    @EqualsAndHashCode.Exclude
    private final Set<RconIpScanIpEntity> ipScanIpEntitySet = new HashSet<>();

    public void addRconIpScanIpEntity(RconIpScanIpEntity entity) {
        ipScanIpEntitySet.add(entity);
    }
}
