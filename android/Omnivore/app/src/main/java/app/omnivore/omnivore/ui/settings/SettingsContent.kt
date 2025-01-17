package app.omnivore.omnivore.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.omnivore.omnivore.BuildConfig
import app.omnivore.omnivore.R
import app.omnivore.omnivore.Routes
import app.omnivore.omnivore.ui.auth.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
  loginViewModel: LoginViewModel,
  settingsViewModel: SettingsViewModel,
  navController: NavHostController,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.settings_view_title)) },
        actions = {
          IconButton(onClick = { navController.navigate(Routes.Library.route) }) {
            Icon(
              imageVector = Icons.Default.Home,
              contentDescription = null
            )
          }
        }, colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      )
    }
  ) { paddingValues ->
    SettingsViewContent(
      loginViewModel = loginViewModel,
      settingsViewModel = settingsViewModel,
      navController = navController,
      modifier = Modifier
        .padding(
          top = paddingValues.calculateTopPadding(),
          bottom = paddingValues.calculateBottomPadding()
        )
    )
  }
}

@Composable
fun SettingsViewContent(loginViewModel: LoginViewModel, settingsViewModel: SettingsViewModel, navController: NavHostController, modifier: Modifier) {
  val showLogoutDialog = remember { mutableStateOf(false)  }
  val showManageAccountDialog = remember { mutableStateOf(false ) }

  Box(
    modifier = modifier.fillMaxSize()
  ) {

    val version = "Omnivore Version: " + BuildConfig.VERSION_NAME

    Column(
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start,
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
        .padding(horizontal = 6.dp)
        .verticalScroll(rememberScrollState())
    ) {


      SettingRow(text = stringResource(R.string.settings_view_setting_row_documentation)) {
        navController.navigate(Routes.Documentation.route)
      }
      RowDivider()
      SettingRow(text = stringResource(R.string.settings_view_setting_row_feedback)) {
        settingsViewModel.presentIntercom()
      }
      RowDivider()
      SettingRow(text = stringResource(R.string.settings_view_setting_row_privacy_policy)) {
        navController.navigate(Routes.PrivacyPolicy.route)
      }
      RowDivider()
      SettingRow(text = stringResource(R.string.settings_view_setting_row_terms_and_conditions)) {
        navController.navigate(Routes.TermsAndConditions.route)
      }

      SectionSpacer()

      SettingRow(text = stringResource(R.string.settings_view_setting_row_manage_account)) {
        showManageAccountDialog.value = true
      }
      RowDivider()
      SettingRow(text = stringResource(R.string.settings_view_setting_row_logout), includeIcon = false) {
        showLogoutDialog.value = true
      }
      RowDivider()

      Text(
        text = version,
        fontSize = 12.sp,
        modifier = Modifier
          .padding(15.dp)
      )
    }

    if (showLogoutDialog.value) {
      LogoutDialog { performLogout ->
        if (performLogout) {
          loginViewModel.logout()
        }
        showLogoutDialog.value = false
      }
    }

    if (showManageAccountDialog.value) {
      ManageAccountDialog(
        onDismiss = { showManageAccountDialog.value = false },
        settingsViewModel = settingsViewModel
      )
    }
  }
}

@Composable
private fun RowDivider() {
  Divider(
    modifier = Modifier.padding(horizontal = 12.dp),
    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
  )
}

@Composable
private fun SectionSpacer() {
  RowDivider()
  Spacer(Modifier.height(60.dp))
  RowDivider()
}

@Composable
private fun SettingRow(text: String, includeIcon: Boolean = true, tapAction: () -> Unit) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { tapAction() }
  ) {
    Text(
      text = text,
      modifier = Modifier
        .align(Alignment.CenterVertically)
        .padding(16.dp),
      style = MaterialTheme.typography.titleMedium
    )

    if (includeIcon) {
      Icon(
        painter = painterResource(id = R.drawable.chevron_right),
        contentDescription = null
      )
    }
  }
}
