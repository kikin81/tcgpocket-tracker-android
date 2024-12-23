/*
 * Copyright 2024
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.kikin.android.ptp.cards

import androidx.annotation.StringRes
import us.kikin.android.ptp.R

enum class CardsFilterType(
    val setId: String,
    @StringRes val titleResId: Int,
) {
    All("All", R.string.card_list_filter_all),
    GeneticApex("Genetic Apex  (A1)", R.string.card_list_filter_genetic_apex),
    PromoA("Promo-A", R.string.card_list_filter_promo_a),
    MythicalIslands("Mythical Island  (A1a)", R.string.card_list_filter_mythical_islands),
}
