package com.example.birdline.models

import android.util.Base64
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Encriptacion {
    private val CIPHER_TRANSFORM ="AES/CBC/PKCS5PADDING"

    fun cifar(textoPlano:String,llave:String): String {
        val cipher:Cipher = Cipher.getInstance(CIPHER_TRANSFORM)
        val llaveByteFinal = ByteArray(16)
        val llaveByteOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveByteOriginal,
            0,
            llaveByteFinal,
            0,
            Math.min(
                llaveByteOriginal.size,
                llaveByteFinal.size
            )
        )

        val secretKey:SecretKey = SecretKeySpec(
            llaveByteFinal,
            "AES"
        )
        val vectorInicializacion = IvParameterSpec(llaveByteFinal)
        cipher.init(Cipher.ENCRYPT_MODE,
            secretKey,
            vectorInicializacion)

        val textoCifrado = cipher.doFinal(textoPlano.toByteArray(
            charset("UTF-8")
        ))
        var resultado = String(textoCifrado) //Resultado con carecteres raros

        //Segundo CIFRADO, El mismisimo cifrado en base64
        var resultadoEnBase = String(Base64.encode(textoCifrado,Base64.NO_PADDING))


        return resultadoEnBase
    }

    fun descifar(textoCifrado:String,llave:String): String {
        val textoCifradoBytes = Base64.decode(textoCifrado,Base64.NO_PADDING)

        val cipher:Cipher = Cipher.getInstance(CIPHER_TRANSFORM)
        val llaveByteFinal = ByteArray(16)
        val llaveByteOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveByteOriginal,
            0,
            llaveByteFinal,
            0,
            Math.min(
                llaveByteOriginal.size,
                llaveByteFinal.size
            )
        )

        val secretKey:SecretKey = SecretKeySpec(
            llaveByteFinal,
            "AES"
        )
        val vectorInicializacion = IvParameterSpec(llaveByteFinal)
        cipher.init(Cipher.DECRYPT_MODE,
            secretKey,
            vectorInicializacion)

        val textoPlano = String(cipher.doFinal(textoCifradoBytes))

        return textoPlano


    }
}