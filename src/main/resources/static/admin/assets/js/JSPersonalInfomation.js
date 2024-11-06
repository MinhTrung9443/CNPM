const $$ = document.querySelectorAll.bind(document)
const $ = document.querySelector.bind(document)
const myAccount =$('.nav__information-personal-account')
const myAccountList = $('.nav__information-my-account-list')
const purchaseOrder =$('.nav__information-personal-purchase')
const purchaseOrderName =$('.nav__information-personal-purchase .nav__information-personal-name')
const purchaseOrderMain =$('.purchase-order')
const myAccountMain = $('.my-account')
const tabsAccount = $$('.nav__information-my-account-item')
const panesAccount = $$('.my-account__main')
const tabActive = $('.my-account__main.active-tab')
myAccount.addEventListener('click', function (){
    myAccountList.style.display="block"
    purchaseOrderName.classList.remove('active-btn')
    purchaseOrderMain.style.display="none";
    myAccountMain.style.display="block"
})
purchaseOrder.addEventListener("click",function (){
    myAccountList.style.display="none"
    purchaseOrderName.classList.add('active-btn')
    purchaseOrderMain.style.display="block"
    myAccountMain.style.display="none"
})
tabsAccount.forEach((tab,index)=>{
    const  pane = panesAccount[index]
    tab.onclick = function (){
        $('.nav__information-my-account-item.active-btn').classList.remove('active-btn')
        $('.my-account__main.active-tab').classList.remove('active-tab')
        tab.classList.add('active-btn')
        this.classList.add('active-tab')
        pane.classList.add('active-tab')
    }
})
const addAddressBtn = $('.my-account__top-btn')
const modalAddress = $('.modal-address')
const addressForm = $('.address-form')
const backBtn = $('.auth-form__controls-back')
addAddressBtn.addEventListener('click', function (){
    modalAddress.style.display="flex"
})
modalAddress.addEventListener('click',function (){
    modalAddress.style.display="none"
})
addressForm.addEventListener('click', function (e){
    e.stopPropagation();
})
// backBtn.addEventListener('click',function (){
//     modalAddress.style.display="none"
// })

const statusOrder = $$('.nav__information-orders-item')
const panesOrder =$$('.nav__information-orders-content')

statusOrder.forEach((tab, index)=>{
    const pane = panesOrder[index];
    tab.onclick =function (){
        $('.nav__information-orders-item.active-btn').classList.remove('active-btn')
        $('.nav__information-orders-content.active-tab').classList.remove('active-tab')
        tab.classList.add('active-btn')
        this.classList.add('active-tab')
        pane.classList.add('active-tab')
    }
})
const orderReviewBtn = $$('.order-review')
orderReviewBtn.forEach((btn,index)=>{
    btn.onclick = function () {
        $('.modal-review').classList.add('modal-review__open')
    }
})
const back= $('.back-btn')
const modalReview = $('.modal-review')
back.addEventListener('click', function (){
    $('.modal-review').classList.remove('modal-review__open')
})
$('.modal-review').onclick = function (e){
    this.classList.remove("modal-review__open")
}
const formReview = $('.review-form')
formReview.addEventListener('click', function (e){
    e.stopPropagation()
})

const btnLogout = $(".my-account-logout");
btnLogout.addEventListener("click", showAccount);






