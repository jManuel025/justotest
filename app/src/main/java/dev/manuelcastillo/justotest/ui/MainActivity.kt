package dev.manuelcastillo.justotest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import dev.manuelcastillo.justotest.R
import dev.manuelcastillo.justotest.data.model.RandomUser
import dev.manuelcastillo.justotest.databinding.ActivityMainBinding
import dev.manuelcastillo.justotest.utils.ResponseStatus
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNextProfile.setOnClickListener {
            viewModel.getRandomUser()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainUIState.collect {
                    it.randomUser?.let { randomUser ->
                        setUserInfo(randomUser)
                    }
                    when(it.status) {
                        ResponseStatus.Success -> {
                            binding.svMainContent.visibility = View.VISIBLE
                            binding.cpiMain.visibility = View.GONE
                            binding.tvMainError.visibility = View.GONE
                        }
                        ResponseStatus.Error -> {
                            binding.svMainContent.visibility = View.GONE
                            binding.cpiMain.visibility = View.GONE
                            binding.tvMainError.visibility = View.VISIBLE
                        }
                        ResponseStatus.Loading -> {
                            binding.svMainContent.visibility = View.GONE
                            binding.cpiMain.visibility = View.VISIBLE
                            binding.tvMainError.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setUserInfo(randomUser: RandomUser) {
        with(binding) {
            tvProfileName.text = randomUser.name
            tvProfileLocation.text = randomUser.country
            Glide.with(binding.root).load(randomUser.picture).into(binding.ivProfilePhoto)
            ivProfileGender.setBackgroundResource(
                if(randomUser.gender == "male") R.drawable.ic_male else R.drawable.ic_female
            )
            ivProfileGender.contentDescription = getString(R.string.profile_gender_cd, randomUser.gender)

            tvAddress.text = randomUser.address
            tvAddress.setOnClickListener {
                seeLocation(randomUser.locationLat, randomUser.locationLng, randomUser.address)
            }

            tvAboutInfoUsername.text = getString(R.string.about_username, randomUser.username)
            tvAboutInfoBirthDate.text = getString(R.string.about_birth_date, randomUser.birthDate)
            tvAboutInfoRegisterDate.text = getString(R.string.about_member_since, randomUser.registerDate)

            tvContactInfoPhone.text = randomUser.phone
            tvContactInfoEmail.text = randomUser.email
        }
    }

    private fun seeLocation(lat: Double, lng: Double, address: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lng?q=$lat,$lng($address)"))
        startActivity(intent)
    }
}