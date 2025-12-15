package dev.deftu.seamflow.core.protocol.discovery

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.DeviceType
import dev.deftu.seamflow.core.protocol.link.Link
import dev.deftu.seamflow.core.protocol.link.LinkInfo

object HostElection {
    fun decide(
        localId: DeviceId,
        localType: DeviceType,
        localLink: LinkInfo,
        remoteId: DeviceId,
        remoteType: DeviceType,
        remoteLink: LinkInfo,
    ): DeviceId {
        val localScore = score(localType, localLink)
        val remoteScore = score(remoteType, remoteLink)

        return when {
            localScore > remoteScore -> localId
            remoteScore > localScore -> remoteId
            else -> if (localId.value >= remoteId.value) localId else remoteId
        }
    }

    fun score(type: DeviceType, link: LinkInfo): Int {
        var score = 0

        // Computers will generally have access to more resources
        score += when {
            type.isComputer -> 200
            type.isMobile -> 120
            type.isWeb -> 60
            else -> 80
        }

        // Prioritize connections over IP
        score += if (Link.Capability.IP in link.capabilities) 80 else -500

        // Prioritize high bandwidth links
        score += when (link.type) {
            Link.Type.LAN -> 50
            Link.Type.HOTSPOT -> 45
            Link.Type.TETHER -> 40
            Link.Type.DIRECT -> 35
            Link.Type.BLUETOOTH -> 0 // Low bandwidth
        }

        // Bandwidth...
        score += if (Link.Capability.HIGH_BANDWIDTH in link.capabilities) 10 else 0
        // Latency...
        score += if (Link.Capability.LOW_LATENCY in link.capabilities) 5 else 0

        return score
    }
}
