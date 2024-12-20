import json
import sqlite3
import re

with open('genetic_apex_cards.json', "r") as file:
    data = json.load(file)

conn = sqlite3.connect("pokemon_cards.db")

cursor = conn.cursor()

# Create tables
cursor.execute("""
CREATE TABLE IF NOT EXISTS Cards (
    id TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    hp TEXT,
    card_type TEXT,
    evolution_type TEXT,
    image TEXT,
    weakness TEXT,
    retreat INTEGER,
    rarity TEXT,
    fullart TEXT,
    ex TEXT,
    set_details TEXT,
    pack TEXT,
    artist TEXT,
    crafting_cost INTEGER
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS Abilities (
    card_id TEXT,
    name TEXT,
    effect TEXT,
    FOREIGN KEY(card_id) REFERENCES Cards(id)
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS Attacks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card_id TEXT,
    name TEXT,
    damage TEXT,
    effect TEXT,
    FOREIGN KEY(card_id) REFERENCES Cards(id)
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS AttackCosts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    attack_id INTEGER,
    cost TEXT,
    FOREIGN KEY(attack_id) REFERENCES Attacks(id)
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS AlternateVersions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card_id TEXT,
    version TEXT,
    rarity TEXT,
    FOREIGN KEY(card_id) REFERENCES Cards(id)
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS Probabilities (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card_id TEXT,
    range TEXT,
    probability TEXT,
    FOREIGN KEY(card_id) REFERENCES Cards(id)
)
""")

def generate_unique_card_id(set_details: str, card_id: str) -> str:
    """
    Generates a unique card ID based on the set details and card ID.

    Args:
        set_details (str): The set details string (e.g., "Genetic Apex  (A1)").
        card_id (str): The card ID within the set.

    Returns:
        str: A unique card ID (e.g., "A1001").
    """
        # Predefined mapping for sets without parentheses
    predefined_identifiers = {
        "Promo-A": "P-A",
    }
    # Check if set_details matches a predefined identifier
    if set_details in predefined_identifiers:
        set_identifier = predefined_identifiers[set_details]
    else:
        # Attempt to extract set identifier dynamically from parentheses
        match = re.search(r"\((.*?)\)", set_details)
        set_identifier = match.group(1) if match else "UNKNOWN"

    # Zero-pad the card ID to 3 digits
    card_id_padded = str(card_id).zfill(3)

    # Combine the set identifier and the padded card ID
    unique_card_id = f"{set_identifier}{card_id_padded}"

    return unique_card_id

# Insert data into tables
for card in data:
    # Convert retreat to None if it's missing or not applicable
    retreat_value = None if card["retreat"] in ["", "N/A"] else int(card["retreat"])
    
    # Convert weakness to None if it's missing or not applicable
    weakness_value = None if card["weakness"] in ["", "N/A"] else card["weakness"]

    # Convert the crafting cost to None if it's missing or not an integer
    crafting_cost = None if card["crafting_cost"] in ["", "Unknown"] else int(card["crafting_cost"])

    card_id = generate_unique_card_id(card["set_details"], card["id"])

    print("Inserting card with ID:", card["id"])
    cursor.execute("""
    INSERT INTO Cards (id, name, hp, card_type, evolution_type, image, weakness, retreat, rarity, fullart, ex, set_details, pack, artist, crafting_cost)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """, (
        card_id, card["name"], card["hp"], card["card_type"], card["evolution_type"], 
        card["image"], weakness_value, retreat_value, card["rarity"], card["fullart"], 
        card["ex"], card["set_details"], card["pack"], card["artist"], crafting_cost
    ))

    # Insert ability only if it is not "No ability"
    ability = card.get("ability")
    if isinstance(ability, dict):
        # Process the ability if it's a dictionary
        if ability.get("name") != "No ability":
            cursor.execute("""
            INSERT INTO Abilities (card_id, name, effect)
            VALUES (?, ?, ?)
            """, (
                card["id"], ability.get("name"), ability.get("effect")
            ))
    elif isinstance(ability, str):
        # Handle cases where 'ability' is a string
        if ability != "No ability":
            cursor.execute("""
            INSERT INTO Abilities (card_id, name, effect)
            VALUES (?, ?, ?)
            """, (
                card["id"], ability, None  # Insert the string as 'name' and use None for 'effect'
            ))
    else:
        # Handle missing or unexpected 'ability'
        print(f"Card {card['id']} has an unexpected ability format: {ability}")

    # Insert attacks (if any)
    for attack in card["attacks"]:
        cursor.execute("""
        INSERT INTO Attacks (card_id, name, damage, effect)
        VALUES (?, ?, ?, ?)
        """, (
            card["id"], attack["name"], attack["damage"], attack["effect"]
        ))
        attack_id = cursor.lastrowid
        for cost in attack["cost"]:
            cursor.execute("""
            INSERT INTO AttackCosts (attack_id, cost)
            VALUES (?, ?)
            """, (attack_id, cost))

    # Insert alternate versions
    for version in card["alternate_versions"]:
        cursor.execute("""
        INSERT INTO AlternateVersions (card_id, version, rarity)
        VALUES (?, ?, ?)
        """, (card["id"], version["version"], version["rarity"]))

    # Insert probabilities
    for range_key, probability in card["probability"].items():
        cursor.execute("""
        INSERT INTO Probabilities (card_id, range, probability)
        VALUES (?, ?, ?)
        """, (card["id"], range_key, probability))

# Commit changes and close connection
conn.commit()
conn.close()

print("Database created and data inserted successfully!")