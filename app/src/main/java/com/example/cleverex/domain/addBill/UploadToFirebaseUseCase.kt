package com.example.cleverex.domain.addBill

import com.google.firebase.storage.FirebaseStorage

class UploadToFirebaseUseCase() {
    val storage = FirebaseStorage.getInstance().reference

}
