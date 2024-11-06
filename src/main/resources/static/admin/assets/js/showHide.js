const $$ = document.querySelectorAll.bind(document)
const $ = document.querySelector.bind(document)

//modal show hide search
const iconSearch = $('.search-link')
const modal =  $('.modal')
const iconClose = $('.modal__icon-close')
const inputSearch = $('.modal__input')
const searchHistory = $('.modal__history')

function showSearch () {
    modal.classList.add('modal__open')
}
function hideSearch () {
    modal.classList.remove('modal__open')
}

iconSearch.addEventListener('click', showSearch)
iconClose.addEventListener('click', hideSearch)
modal.addEventListener('click',hideSearch)
inputSearch.addEventListener('click',function(event){
    // ngừng việc nổi bọt
    event.stopPropagation()
})
searchHistory.addEventListener('click',function(event){
    // ngừng việc nổi bọt
    event.stopPropagation()
})

//Modal show hide account
const modalAccount = $('.modal-account')
const iconUsers = $$('.login-link')
const authForms = $$('.auth-form')
const registerForm = $('.register-form')
const  loginForm = $('.login-form')
const loginFormBtn = $('.login-from__btn')
const registerFormBtn = $('.register-from__btn')
const backBtn= $$('.auth-form__controls-back')
const navMobile = $('.nav-mobile')
const navBtnMobile = $('.nav-link')
const forgotBtn = $('.auth-form__help-link')
const forgotForm = $('.forgot-pass-form')



function showAccount(){
    modalAccount.classList.add('modal-account__open')
}
function hideAccount(){
    modalAccount.classList.remove('modal-account__open')
}
for(const iconUser of iconUsers) {
    iconUser.addEventListener('click', showAccount)
}
modalAccount.addEventListener('click',hideAccount)

loginFormBtn.addEventListener('click', function (){
    loginForm.style.display="block"
    registerForm.style.display="none"

})
registerFormBtn.addEventListener('click', function (){
    registerForm.style.display="block"
    loginForm.style.display="none"

})
forgotBtn.addEventListener('click', function (){
    loginForm.style.display="none"
    forgotForm.style.display="block"
})

for(const btn of backBtn){
    btn.addEventListener('click', hideAccount)
}

for(const authForm of authForms) {
    authForm.addEventListener('click', function (e) {
        e.stopPropagation()
    })
}


const close = $('.close');
const overlay = $('.popup-overlay');
const popup = $('.popup');

popup.onclick = function (e) {
    e.stopPropagation();
}
close.onclick = function(e) {
    document.querySelector('.stay-in-touch').style.display = 'none';
    overlay.style.display = 'none';
}
overlay.onclick = function (){
    document.querySelector('.stay-in-touch').style.display = 'none';
    overlay.style.display = 'none';
}

const submitLink = $(".auth-form__controls a");
    submitLink.onclick = function (){
        submitLink.setAttribute("href", "./personalInformation.html");
}

const navMobileOverlay = $('.nav-mobile__overlay');

navBtnMobile.onclick = function() {
    navMobile.style.display = "block";
    navMobileOverlay.style.display = "block";
}

const iconExit = $('.icon-exit');

iconExit.onclick = function () {
    navMobile.style.display = "none";
    navMobileOverlay.style.display = "none";
}

navMobileOverlay.onclick = function() {
    navMobile.style.display = "none";
    navMobileOverlay.style.display = "none"; 
}