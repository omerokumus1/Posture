# Posture
Ömer Faruk Okumuş

Bu uygulama bir prototiptir. Uygulamanın testleri uçtan uca yapılmamış olup emülatörde ve Huawei P Smart isimli cihazda çalıştırılabilir. 
Unit testlerle fonksiyonellik testleri yapılmış ve belirli senaryolar hem emülatörde hem de Huawei P Smart cihazında test edilmiştir.
Prototip olduğundan dolayı kısıtlamaları mevcuttur. 

* Tanıtım Videosu

https://user-images.githubusercontent.com/76251918/168494965-cbb467df-ef90-401f-b8cd-778ed9389c97.mp4




* Uygulamanın Nasıl Indirileceği ve çalıştırılacağı: 
Uygulama Huawei AppGallery Console üzerinden yapılanmış olup macos ortamında geliştirilmiştir. Hem emülatörde hem de Huawei P Smart isimli cihazda testleri yapıldı. Proje github üzerinden direkt alınıp android studio bir emülatör ile veya huawei bir cihaz ile çalıştırılabilir (Cihaz bazlı sorunlar olabilir). Dependency sorunları oluşursa clean project -> invalidate caches -> gradle sync -> make/rebuild project adımları izlenebilir.

Aynı zamanda aşağıdaki apk aracılığıyla direkt yükleme yapılabilir.

[app-release.apk.zip](https://github.com/omerokumus1/Posture/files/8696104/app-release.apk.zip)

* Proje geliştirilirken kullanılan teknolojiler ve 3. parti kütüphaneler (Dependency olarak eklenen kütüphaneler):
  1. Android Fragments: Hızlı ekran geçişleri sağlamak için kullanıldı.
  2. Android ViewModel: Bussiness Logic katmanını sağlamak için kullanıldı.
  3. Android Navigation: Ekran geçişlerini kolaylaştırmak için kullanıldı.
  4. ViewBinding: Light-weight layout inflation işlemleri için kullanıldı
  5. Dagger-Hilt kütüphanesi: Dependency Injection için kullanıldı.
  6. Mockito ve JUnit4: UnitTesting
  7. Skeleton Detection Full SDK: Kullanılan Huawei servisi.
