let addButton = document.querySelector(".pay__user-info__body-add");
addButton.onclick = function () {
    document.querySelector(".modal-address").style.display = "flex";

}
let backButton = document.querySelector(".auth-form__controls-back")

backButton.onclick = function () {
    document.querySelector(".modal-address").style.display = "none";
}

let modalOverlay = document.querySelector(".modal__overlay");

modalOverlay.onclick = function () {
    document.querySelector(".modal-address").style.display = "none";
}

let confirmPurchase = document.querySelector(".pay-confirmation-button button");

confirmPurchase.onclick = function() {
    document.querySelector(".popup-confirm-pay__wrapper").style.display = "block";
    document.querySelector(".popup-confirm-pay__body").style.display = "flex";
    document.querySelector(".popup-confirm-pay__body--done").style.display = "none";
}

let cancelPurchase = document.querySelector(".cancel-purchase-js");

cancelPurchase.onclick = function () {
    document.querySelector(".popup-confirm-pay__wrapper").style.display = "none";
}

let confirmPurchase2 = document.querySelector(".confirm-purchase-js");

confirmPurchase2.onclick = function() {
    document.querySelector(".popup-confirm-pay__body").style.display = "none";
    document.querySelector(".popup-confirm-pay__body--done").style.display = "block";
}

let overlayPurchase = document.querySelector(".popup-confirm-pay__wrapper");

overlayPurchase.onclick = function() {
    document.querySelector(".popup-confirm-pay__wrapper").style.display = "none";
}

let confirmPay = document.querySelector(".popup-confirm-pay");

confirmPay.onclick = function (e) {
    e.stopPropagation();
}