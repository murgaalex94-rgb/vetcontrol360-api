import hashlib
import base64
import os
import secrets

# Configuración igual a PasswordHasher.java
ALGORITHM = "PBKDF2WithHmacSHA256"  # En Python usamos pbkdf2_hmac con SHA256
ITERATIONS = 100_000
KEY_LENGTH = 256  # bits = 32 bytes
SALT_BYTES = 16

def hash_password(password):
    # Generar salt aleatorio
    salt = os.urandom(SALT_BYTES)
    
    # Generar hash usando PBKDF2-HMAC-SHA256
    hash_bytes = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode('utf-8'),
        salt,
        ITERATIONS,
        KEY_LENGTH // 8  # Convertir bits a bytes
    )
    
    # Codificar en Base64
    salt_b64 = base64.b64encode(salt).decode('utf-8')
    hash_b64 = base64.b64encode(hash_bytes).decode('utf-8')
    
    # Formato: ITERATIONS:SALT:HASH
    return f"{ITERATIONS}:{salt_b64}:{hash_b64}"

# Generar hash para la contraseña solicitada
password = "AlexMurga.20*"
password_hash = hash_password(password)
print(f"Password: {password}")
print(f"Hash: {password_hash}")
