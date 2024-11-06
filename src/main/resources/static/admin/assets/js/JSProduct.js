let close = document.querySelector('.close');
let overlay = document.querySelector('.popup-overlay');

document.querySelector('.popup').onclick = function (e) {
    e.stopPropagation();
}

close.onclick = function() {
    document.querySelector('.stay-in-touch').style.display = 'none';
    overlay.style.display = 'none';
}

overlay.onclick = function() {
    document.querySelector('.stay-in-touch').style.display = 'none';
    overlay.style.display = 'none';
}


let quantityWantToBuy = document.querySelector('.quantity-buttons--number').innerText;

document.querySelector('.quantity-buttons--minus').onclick = function() {
    quantityWantToBuy > 1 ? quantityWantToBuy -= 1 : quantityWantToBuy = 1;
    document.querySelector('.quantity-buttons--number').innerText = quantityWantToBuy;
}

document.querySelector('.quantity-buttons--plus').onclick = function() {
    quantityWantToBuy = Number(quantityWantToBuy) + 1;
    document.querySelector('.quantity-buttons--number').innerText = quantityWantToBuy;
}

const $ = document.querySelector.bind(document)
const $$ = document.querySelectorAll.bind(document)

const tabs = $$('.product-content__tabs-item')
const panes = $$('.product-content__tabs-content > div')

const tabActive = $('.product-content__tabs-item.tabs-active')

tabs.forEach((tab, index) => {
    const pane = panes[index]
    tab.onclick = function() {
        $('.product-content__tabs-item.tabs-active').classList.remove('tabs-active')
        $('.tabs-content__item.tabs-active').classList.remove('tabs-active')

        this.classList.add('tabs-active')
        pane.classList.add('tabs-active')
    }
});
