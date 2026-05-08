# EStore – Plateforme E-Commerce Full Stack

## Réalisé par
- HAFSA MOUNIB
- YASMINE MOUMEN

## Encadré par
Pr. ZAHOUR OMAR – FSBM, Université Hassan II de Casablanca

## Technologies
| Couche | Technologies |
|--------|-------------|
| Backend | Spring Boot 4, JPA, H2, MongoDB, Spring Security, JWT |
| Frontend | Angular 21, TypeScript |
| Build | Maven, npm |

## Structure du projet
- `estore-backend/` → API REST Spring Boot (port 8080)
- `estore-frontend/` → Interface Angular (port 4200)

## Lancer le projet

### Backend
```bash
cd estore-backend
mvn spring-boot:run
```

### Frontend
```bash
cd estore-frontend
npm install
ng serve
```

## Comptes de test
| Email | Mot de passe | Rôle |
|-------|-------------|------|
| user@estore.com | user123 | USER |
| admin@estore.com | admin123 | ADMIN |

## Fonctionnalités
- Authentification JWT + BCrypt
- Catalogue produits avec filtrage
- Gestion du panier
- Validation de commande
- Avis produits (MongoDB)
- Profil utilisateur (Phone, Address, City, Country)
