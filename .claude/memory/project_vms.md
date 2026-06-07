---
name: project-vms-overview
description: Visitor Management System Android app — structure, theme, and screens overview
metadata:
  type: project
---

Visitor Management System (VMS) Android app. Java + ConstraintLayout/LinearLayout, Material3 theme. minSdk 24, targetSdk 35.

**Screens:**
- MainActivity → welcome/splash → navigates to LoginActivity
- LoginActivity → login form → HomeActivity (login btn) or SignupActivity (signup btn)
- SignupActivity → registration form → back to LoginActivity
- HomeActivity → dashboard with "Pending Requests" and "Live Work Permits" tables
- RequestdashboardActivity → access request submission form

**Theme applied (2026-06-07):**
- Primary: Deep Navy #1A237E, Accent: Cyan #00BCD4, Background: #EEF2F7
- Custom drawables: bg_splash (gradient), bg_card, bg_input_field, bg_button_primary, bg_button_outline, bg_nav_item, bg_table_header, bg_status_pending
- All layouts use string resources (strings.xml) and color references (colors.xml)
- Styles defined in themes.xml: InputStyle, ButtonStyle.Primary, ButtonStyle.Outline, TextStyle.*

**Why:** User requested beautification with proper Android practices (strings, colors, themes).
**How to apply:** Any future UI changes should follow the established navy/cyan palette and use the defined styles/drawables.
