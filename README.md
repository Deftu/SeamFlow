# SeamFlow

## Work-in-Progress
SeamFlow is an open-source, cross-device linking application designed to seamlessly connect your devices. Whether you're transferring files, sharing clipboard content, or managing notifications.

---

[![Discord Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/social/discord-singular_64h.png)](https://s.deftu.dev/discord)
[![Ko-Fi Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/donate/kofi-singular_64h.png)](https://s.deftu.dev/kofi)

---

## Roadmap / Feature List

### Platform Support

> [!WARNING]
> iOS and macOS have significant limitations imposed by Apple that restrict feature availability compared to other platforms.
> Neither will be prioritized for any kind of initial development; the primary focus will be on Windows, Linux and Android.

- [ ] Windows
- [ ] Linux
- [ ] macOS
- [ ] Android
- [ ] iOS
- [ ] Web

### Core Features

> [!WARNING]
> Feature availability varies by platform due to operating system limitations.
> Some features (e.g. notification replies, SMS, remote control) are only supported on specific platforms.

- [ ] Several connection methods
    - [ ] Local Network (Wi-Fi)
    - [ ] Hotspot
    - [ ] Wi-Fi Direct
    - [ ] USB Tethering
    - [ ] Bluetooth
- [ ] Trust & security system
    - [ ] Device pairing with verification
    - [ ] End-to-end encryption for all data transfers
    - [ ] Permission management for features
    - [ ] Two-way device revocation
- [ ] Multi-device access point systems (one device acting as a relay or hub for others)
- [ ] Basic device telemetry visibility
    - [ ] Battery status
    - [ ] Network status
    - [ ] Storage status
    - [ ] Device information
- [ ] File transfers
    - [ ] Large file support
    - [ ] Multiple file selection
    - [ ] Folder transfers
    - [ ] Transfer resumption
    - [ ] Transfer queuing
- [ ] Clipboard sharing
    - [ ] Text
    - [ ] Images
    - [ ] Files
    - [ ] Request clipboard content from other devices
- [ ] Notification mirroring
    - [ ] Dismiss notifications on all devices
    - [ ] Reply to notifications from any device
    - [ ] Custom notification settings per device
    - [ ] Notification history
- [ ] "Find My Device" feature
    - [ ] Ring device
    - [ ] Lock device remotely
- [ ] Call forwarding and management
    - [ ] Answer/reject calls from any device
    - [ ] Call logs access
    - [ ] Mute/unmute calls
    - [ ] Speakerphone toggle
- [ ] SMS/MMS management (Android only)
    - [ ] Send and receive SMS/MMS from any device
    - [ ] Group messaging support
    - [ ] Message search functionality
    - [ ] Media attachments support
- [ ] Contact synchronization
    - [ ] View and manage contacts from any device
    - [ ] Two-way contact synchronization
    - [ ] Contact grouping and organization
    - [ ] Import/export contacts
- [ ] Remote device control
    - [ ] Remote screen viewing
    - [ ] Remote input control (keyboard and mouse)
    - [ ] File system access
    - [ ] Application management

### Non-Goals For Now

- Cloud-based relay servers (local-first by design)
- Account-based login requirements
- Forced internet connectivity

---

## But how will this be funded?

SeamFlow is **currently** being developed as a completely free and open-source project.
However, the only way to keep development sustainable for the time being is through donations and sponsorships.

This does NOT mean the app may remain entirely "free" forever, as there may come a time in the future where certain
parts of the app may include advertisements or premium features to help support ongoing development costs.

https://s.deftu.dev/kofi

---

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/8fb6621b-811a-473b-9087-c8c42b50e74c.png)](https://s.deftu.dev/bisect)

---

**This project is licensed under [LGPL-3.0][lgpl]**\
**&copy; 2025 Deftu**

[lgpl]: https://www.gnu.org/licenses/lgpl-3.0.en.html
