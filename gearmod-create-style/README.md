# Gear Mod (NeoForge 1.21.1) - Create-mod-style kinetic system

Mod ini sekarang punya 4 block:

1. **Rotating Gear (Simple)** (`gearmod:rotating_gear`) — versi paling awal, muter sendiri terus tanpa syarat. Dibiarin ada buat referensi/pembanding.
2. **Hand Crank** (`gearmod:hand_crank`) — klik kanan buat kasih tenaga ke jaringan selama ~5 detik. Ini **satu-satunya sumber tenaga**.
3. **Shaft** (`gearmod:shaft`) — poros penghubung, ikut muter kalau nyambung (langsung/berantai) ke hand crank yang lagi aktif.
4. **Gear** (`gearmod:gear`) — gear bergerigi asli, di-render pakai **GeckoLib** (model 3D custom, bukan cube polos), muter berdasarkan kecepatan dari jaringan kinetic yang sama.

Coba susun: `hand_crank` -> `shaft` -> `gear`, lalu klik kanan hand crank-nya. Gear & shaft yang nyambung harusnya ikut muter selama ~5 detik, lalu berhenti.

## PENTING: GeckoLib wajib ada di folder `mods`

Mod ini pakai library **GeckoLib** buat render gear bergerigi. GeckoLib **TIDAK ikut ke-bundle** ke dalam jar hasil build kamu — dia harus ada sebagai jar terpisah di folder `mods`.

1. Download GeckoLib (NeoForge, versi 1.21.1) dari salah satu:
   - Modrinth: https://modrinth.com/mod/geckolib
   - CurseForge: https://www.curseforge.com/minecraft/mc-mods/geckolib
2. Taruh jar GeckoLib itu di folder `mods` bareng jar mod ini.
3. Kalau lupa, world/game bakal crash pas load dengan error "missing dependency: geckolib".

## Cara build jadi .jar

1. Butuh **Java 21** dan koneksi internet (build pertama download NeoForge + GeckoLib).
2. `./gradlew build` (Linux/Mac/Codespaces) atau `gradlew.bat build` (Windows).
3. Jar ada di `build/libs/gearmod-1.0.0.jar`.
4. Test langsung: `./gradlew runClient` (butuh GUI, biasanya nggak jalan di Codespaces headless).

## Kalau build gagal karena GeckoLib

GeckoLib kadang mengganti nama method antar versi kecil (`getModelResource` vs `getModelLocation`, dst). Kalau `compileJava` gagal dengan pesan semacam "method does not override anything" di `GearGeoModel.java`, itu tandanya versi GeckoLib di `gradle.properties` (`geckolib_version=4.8.4`) beda sedikit API-nya. Cara paling gampang benerin:
1. Buka `GearGeoModel.java`
2. Biarkan IDE (atau baca pesan error compiler) kasih tahu nama method yang benar buat versi itu
3. Rename 3 method (`getModelResource`, `getTextureResource`, `getAnimationResource`) sesuai itu

Kalau ada error compile lain, paste ke chat ini, aku bantu benerin.

## Cara kerja sistem kinetic-nya (disederhanakan dari Create)

- **`KineticBlockEntity`** adalah kelas dasar bersama buat hand crank, shaft, dan gear.
- Tiap tick (di server), setiap block non-sumber (shaft & gear) menyerap kecepatan terbesar dari 6 tetangganya. Kalau tetangganya kinetic block juga dan sedang berputar, kecepatan itu "menular".
- Hand crank adalah satu-satunya sumber: saat di-crank, dia set kecepatan tetap selama beberapa detik, lalu balik ke 0.
- Rotasi visual di-extrapolate di client dari waktu sinkronisasi terakhir + kecepatan, jadi animasinya tetap mulus tanpa perlu kirim data tiap tick.
- **Simplifikasi dari Create asli**: nggak ada arah axis (semua muter di sumbu Y / atas-bawah), nggak ada mekanisme "stress/capacity", dan gear-gear nggak saling membalik arah putaran seperti gear mesh asli. Ini fondasi yang bisa dikembangkan lagi.

## Kustomisasi

- **Kecepatan & durasi crank**: `ACTIVE_SPEED` dan `ACTIVE_DURATION_TICKS` di `HandCrankBlockEntity.java`.
- **Bentuk gear**: `assets/gearmod/geo/gear.geo.json` — dibuat manual (hub + 8 gigi), bisa diedit langsung di Blockbench (import sebagai Geckolib/Bedrock geometry) buat bentuk yang lebih detail.
- **Tekstur gear**: `assets/gearmod/textures/block/gear.png` (dipakai model 3D) dan `gear_icon.png` (dipakai ikon di inventory saja, karena render in-world full pakai GeckoLib).
- **Tambah axis/arah** (biar kayak Create beneran, gear nempel di sisi tertentu): perlu tambah `DirectionProperty` di block state + ubah logic propagasi supaya cuma nyambung ke arah tertentu.

## Struktur project

```
src/main/java/com/example/gearmod/
  GearMod.java                         -> registrasi semua block/item/creative tab
  GearModClient.java                   -> registrasi semua renderer
  block/kinetic/KineticBlockEntity.java-> otak jaringan kinetic (base class)
  block/kinetic/HandCrankBlock*.java   -> sumber tenaga
  block/kinetic/ShaftBlock*.java       -> penghubung
  block/kinetic/GearBlock*.java        -> gear bergerigi (GeckoLib)
  client/GearGeoModel.java             -> hubungin ke file model/texture/animasi + inject rotasi
  client/GearGeoRenderer.java          -> renderer GeckoLib buat gear
  client/ShaftRenderer.java            -> renderer vanilla buat shaft
src/main/resources/assets/gearmod/
  geo/gear.geo.json                    -> model 3D gear (hub + gigi)
  animations/gear.animation.json       -> file animasi minimal (rotasi di-drive dari kode)
  textures/block/*.png                 -> semua tekstur (placeholder, bisa diganti)
  blockstates/, models/, lang/         -> asset standar tiap block
```
