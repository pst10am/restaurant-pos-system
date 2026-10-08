# Restaurant POS System

A Java Swing point-of-sale system for a small restaurant. It handles dine-in, takeout and delivery orders, kitchen/receipt printing, caller-ID lookup, delivery-area checks and card payments, backed by a MySQL database.

## Structure

| Component | Package / entry point | What it does |
|-----------|----------------------|--------------|
| **POS terminal** | `src/app_pos` (`app_pos.MainPOS`) | Cashier/server UI: floor plan and tables, order entry, split checks, payments, tips, shift reports. |
| **Admin program** | `src/app_admin` (`app_admin.MainAdmin`) | Back-office UI: menu, categories, options, printers, floor plan/sections, users, order review. |
| **POS server / data layer** | `src/resrc`, `src/model` | DB access (`ResDB`, `ResData`), config loading (`ResCfg`), domain model. *No standalone server process exists yet. Both programs talk to MySQL directly.* |
| Printing | `src/print` | Receipt/kitchen slips via Star printers (JavaPOS). |
| Utilities | `src/loader`, `src/test`, `src/game` | Menu loader, manual test harnesses, a small Snake easter egg. |

## Build and run

Requirements: JDK 7+ and MySQL 5.x.

1. Create a MySQL database and a least-privilege user. Copy `cfg/config.ini.example` to `cfg/config.ini` and fill in the connection string, user and password.
2. Compile (jars are in `lib/`, or import the folder into Eclipse as an existing project):
   ```
   javac -encoding UTF-8 -cp "lib/*;lib/starprn/*" -d bin (all .java files under src/)
   ```
3. Run:
   ```
   run_pos.bat      # POS terminal
   run_admin.bat    # Admin program
   ```
   (equivalently `java -cp "bin;lib/*;lib/starprn/*" app_pos.MainPOS`)

Payment (USAePay) and Google Maps features need your own credentials: set `maps_api_key` in `cfg/config.ini`. `cfg/config.ini` is git-ignored, so never commit real credentials.
