.class public Lcom/musala/atmosphere/android/inject/webviews/InjectedWebViewApplication;
.super Landroid/app/Application;
.source "InjectedWebViewApplication.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 7
    invoke-direct {p0}, Landroid/app/Application;-><init>()V

    return-void
.end method


# virtual methods
.method public onCreate()V
    .locals 2

    .prologue
    .line 10
    invoke-super {p0}, Landroid/app/Application;->onCreate()V

    .line 11
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x13

    if-lt v0, v1, :cond_0

    .line 12
    const/4 v0, 0x1

    invoke-static {v0}, Landroid/webkit/WebView;->setWebContentsDebuggingEnabled(Z)V

    .line 14
    :cond_0
    return-void
.end method
