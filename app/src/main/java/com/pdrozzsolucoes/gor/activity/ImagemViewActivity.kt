package com.pdrozzsolucoes.gor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pdrozzsolucoes.gor.R
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil
import java.io.File

class ImagemViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagem_view)

        var imagemView=findViewById<ImageView>(R.id.image);
        var btnExcluir=findViewById<Button>(R.id.btnExcluir);
        var anexo=File(StorageUtil.getFichaAnexo(this).absolutePath+"/anexo.png");

        btnExcluir.setOnClickListener {
            finish();
            anexo.delete();
        }

        Glide.with(this).load(anexo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imagemView);

    }
}